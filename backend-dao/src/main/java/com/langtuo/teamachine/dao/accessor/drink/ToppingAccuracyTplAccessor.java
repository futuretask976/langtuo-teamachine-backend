package com.langtuo.teamachine.dao.accessor.drink;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drink.ToppingAccuracyTplMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingAccuracyTplPO;
import com.langtuo.teamachine.dao.query.drink.ToppingAccuracyTplQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAccuracyTplAccessor {
    @Resource
    private ToppingAccuracyTplMapper mapper;

    @Resource
    private RedisManager redisManager;

    public ToppingAccuracyTplPO selectOneByCode(String tenantCode, String templateCode) {
        // 首先访问缓存
        ToppingAccuracyTplPO cached = getCache(tenantCode, templateCode, null);
        if (cached != null) {
            return cached;
        }

        ToppingAccuracyTplPO po = mapper.selectOne(tenantCode, templateCode, null);

        // 设置缓存
        setCache(tenantCode, templateCode, null, po);
        return po;
    }

    public ToppingAccuracyTplPO selectOneByName(String tenantCode, String templateName) {
        // 首先访问缓存
        ToppingAccuracyTplPO cached = getCache(tenantCode, null, templateName);
        if (cached != null) {
            return cached;
        }

        ToppingAccuracyTplPO po = mapper.selectOne(tenantCode, null, templateName);

        // 设置缓存
        setCache(tenantCode, null, templateName, po);
        return po;
    }

    public List<ToppingAccuracyTplPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<ToppingAccuracyTplPO> cachedList = getCacheList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<ToppingAccuracyTplPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCacheList(tenantCode, list);
        return list;
    }

    public PageInfo<ToppingAccuracyTplPO> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingAccuracyTplQuery query = new ToppingAccuracyTplQuery();
        query.setTenantCode(tenantCode);
        query.setTemplateCode(StringUtils.isBlank(templateCode) ? null : templateCode);
        query.setTemplateName(StringUtils.isBlank(templateName) ? null : templateName);
        List<ToppingAccuracyTplPO> list = mapper.search(query);

        PageInfo<ToppingAccuracyTplPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingAccuracyTplPO po) {
        int inserted = mapper.insert(po);
        if (inserted == 0) {
            deleteCacheOne(po.getTenantCode(), po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(po.getTenantCode());
        }
        return inserted;
    }

    public int update(ToppingAccuracyTplPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCacheOne(po.getTenantCode(), po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(po.getTenantCode());
        }
        return updated;
    }

    public int delete(String tenantCode, String templateCode) {
        ToppingAccuracyTplPO po = selectOneByCode(tenantCode, templateCode);
        if (po == null) {
            return 0;
        }

        int deleted = mapper.delete(tenantCode, templateCode);
        if (deleted == 1) {
            deleteCacheOne(tenantCode, po.getTemplateCode(), po.getTemplateName());
            deleteCacheList(tenantCode);
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String templateCode, String templateName) {
        return "topping_accuracy_tpl_acc_" + tenantCode + "-" + templateCode + "-" + templateName;
    }

    private String getCacheListKey(String tenantCode) {
        return "topping_accuracy_tpl_acc_" + tenantCode;
    }

    private ToppingAccuracyTplPO getCache(String tenantCode, String templateCode, String templateName) {
        String key = getCacheKey(tenantCode, templateCode, templateName);
        Object cached = redisManager.getValue(key);
        ToppingAccuracyTplPO po = (ToppingAccuracyTplPO) cached;
        return po;
    }

    private List<ToppingAccuracyTplPO> getCacheList(String tenantCode) {
        String key = getCacheListKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<ToppingAccuracyTplPO> poList = (List<ToppingAccuracyTplPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, List<ToppingAccuracyTplPO> poList) {
        String key = getCacheListKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCache(String tenantCode, String templateCode, String templateName, ToppingAccuracyTplPO po) {
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
