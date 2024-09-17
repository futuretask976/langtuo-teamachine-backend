package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.AccuracyTplMapper;
import com.langtuo.teamachine.dao.po.drink.AccuracyTplPO;
import com.langtuo.teamachine.dao.query.drink.AccuracyTplQuery;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AccuracyTplAccessor {
    @Resource
    private AccuracyTplMapper mapper;

    @Resource
    private RedisManager redisManager;

    public AccuracyTplPO getByTplCode(String tenantCode, String templateCode) {
        // 首先访问缓存
        AccuracyTplPO cached = getCache(tenantCode, templateCode, null);
        if (cached != null) {
            return cached;
        }

        AccuracyTplPO po = mapper.selectOne(tenantCode, templateCode, null);

        // 设置缓存
        setCache(tenantCode, templateCode, null, po);
        return po;
    }

    public AccuracyTplPO getByTplName(String tenantCode, String templateName) {
        // 首先访问缓存
        AccuracyTplPO cached = getCache(tenantCode, null, templateName);
        if (cached != null) {
            return cached;
        }

        AccuracyTplPO po = mapper.selectOne(tenantCode, null, templateName);

        // 设置缓存
        setCache(tenantCode, null, templateName, po);
        return po;
    }

    public List<AccuracyTplPO> list(String tenantCode) {
        // 首先访问缓存
        List<AccuracyTplPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<AccuracyTplPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<AccuracyTplPO> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        AccuracyTplQuery query = new AccuracyTplQuery();
        query.setTenantCode(tenantCode);
        query.setTemplateCode(StringUtils.isBlank(templateCode) ? null : templateCode);
        query.setTemplateName(StringUtils.isBlank(templateName) ? null : templateName);
        List<AccuracyTplPO> list = mapper.search(query);

        PageInfo<AccuracyTplPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(AccuracyTplPO po) {
        int inserted = mapper.insert(po);
        if (inserted == CommonConsts.INSERTED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(AccuracyTplPO po) {
        int updated = mapper.update(po);
        if (updated == CommonConsts.UPDATED_ONE_ROW) {
            deleteCacheOne(po.getTenantCode(), po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int deleteByTplCode(String tenantCode, String templateCode) {
        AccuracyTplPO po = getByTplCode(tenantCode, templateCode);
        if (po == null) {
            return CommonConsts.DELETED_ZERO_ROW;
        }

        int deleted = mapper.delete(tenantCode, templateCode);
        if (deleted == CommonConsts.DELETED_ONE_ROW) {
            deleteCacheOne(tenantCode, po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String templateCode, String templateName) {
        return "accuracyTplAcc-" + tenantCode + "-" + templateCode + "-" + templateName;
    }

    private String getCacheListKey(String tenantCode) {
        return "accuracyTplAcc-" + tenantCode;
    }

    private AccuracyTplPO getCache(String tenantCode, String templateCode, String templateName) {
        String key = getCacheKey(tenantCode, templateCode, templateName);
        Object cached = redisManager.getValue(key);
        AccuracyTplPO po = (AccuracyTplPO) cached;
        return po;
    }

    private List<AccuracyTplPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<AccuracyTplPO> poList = (List<AccuracyTplPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<AccuracyTplPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String templateCode, String templateName, AccuracyTplPO po) {
        String key = getCacheKey(tenantCode, templateCode, templateName);
        redisManager.setValue(key, po);
    }

    private void deleteCacheOne(String tenantCode, String templateCode, String templateName) {
        redisManager.deleteKey(getCacheKey(tenantCode, templateCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, templateName));
    }

    private void deleteCacheList(String tenantCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode));
    }
}
