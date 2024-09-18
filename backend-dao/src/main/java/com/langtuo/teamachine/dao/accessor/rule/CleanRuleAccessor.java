package com.langtuo.teamachine.dao.accessor.rule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.rule.CleanRuleMapper;
import com.langtuo.teamachine.dao.po.rule.CleanRulePO;
import com.langtuo.teamachine.dao.query.rule.CleanRuleQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanRuleAccessor {
    @Resource
    private CleanRuleMapper mapper;

    @Resource
    private RedisManager redisManager;

    public CleanRulePO getByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        CleanRulePO cached = setCache(tenantCode, cleanRuleCode);
        if (cached != null) {
            return cached;
        }

        CleanRulePO po = mapper.selectOne(tenantCode, cleanRuleCode);
        setCache(tenantCode, cleanRuleCode, po);
        return po;
    }

    public List<CleanRulePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<CleanRulePO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<CleanRulePO> list = mapper.selectList(tenantCode, null);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public List<CleanRulePO> selectListByCleanRuleCode(String tenantCode, List<String> cleanRuleCodeList) {
        // 这里只是在每台机器初始化的时候会调用，所以先不加缓存
        List<CleanRulePO> list = mapper.selectList(tenantCode, cleanRuleCodeList);
        return list;
    }

    public PageInfo<CleanRulePO> search(String tenantCode, String cleanRuleCode, String cleanRuleName, 
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CleanRuleQuery query = new CleanRuleQuery();
        query.setTenantCode(tenantCode);
        query.setCleanRuleCode(StringUtils.isBlank(cleanRuleCode) ? null : cleanRuleCode);
        query.setCleanRuleName(StringUtils.isBlank(cleanRuleName) ? null : cleanRuleName);
        List<CleanRulePO> list = mapper.search(query);

        PageInfo<CleanRulePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int update(CleanRulePO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int insert(CleanRulePO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int deleteByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        CleanRulePO po = getByCleanRuleCode(tenantCode, cleanRuleCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, cleanRuleCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getCleanRuleCode(), po.getCleanRuleName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String cleanRuleCode) {
        return "cleanRuleAcc-" + tenantCode + "-" + cleanRuleCode;
    }

    private String getCacheListKey(String tenantCode) {
        return "cleanRuleAcc-" + tenantCode;
    }

    private CleanRulePO setCache(String tenantCode, String cleanRuleCode) {
        String key = getCacheKey(tenantCode, cleanRuleCode);
        Object cached = redisManager.getValue(key);
        CleanRulePO po = (CleanRulePO) cached;
        return po;
    }

    private List<CleanRulePO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<CleanRulePO> poList = (List<CleanRulePO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<CleanRulePO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String cleanRuleCode, CleanRulePO po) {
        String key = getCacheKey(tenantCode, cleanRuleCode);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String cleanRuleCode, String cleanRuleName) {
        redisManager.deleteKey(getCacheKey(tenantCode, cleanRuleCode));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
