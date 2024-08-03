package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaPO;
import com.langtuo.teamachine.dao.query.drinkset.TeaQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaAccessor {
    @Resource
    private TeaMapper mapper;

    @Resource
    private RedisManager redisManager;

    public TeaPO selectOneByCode(String tenantCode, String teaCode) {
        // 首先访问缓存
        TeaPO cached = getCachedTea(tenantCode, teaCode, null);
        if (cached != null) {
            return cached;
        }

        TeaPO po = mapper.selectOne(tenantCode, teaCode, null);

        // 设置缓存
        setCachedTea(tenantCode, teaCode, null, po);
        return po;
    }

    public TeaPO selectOneByName(String tenantCode, String teaName) {
        // 首先访问缓存
        TeaPO cached = getCachedTea(tenantCode, null, teaName);
        if (cached != null) {
            return cached;
        }

        TeaPO po = mapper.selectOne(tenantCode, null, teaName);

        // 设置缓存
        setCachedTea(tenantCode, null, teaName, po);
        return po;
    }

    public List<TeaPO> selectList(String tenantCode) {
        // 首先访问缓存
        List<TeaPO> cachedList = getCachedTeaList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaPO> list = mapper.selectList(tenantCode);

        // 设置缓存
        setCachedTeaList(tenantCode, list);
        return list;
    }

    public PageInfo<TeaPO> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaQuery query = new TeaQuery();
        query.setTenantCode(tenantCode);
        query.setTeaName(StringUtils.isBlank(teaName) ? null : teaName);
        query.setTeaCode(StringUtils.isBlank(teaCode) ? null : teaCode);
        List<TeaPO> list = mapper.search(query);

        PageInfo<TeaPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaPO po) {
        return mapper.insert(po);
    }

    public int update(TeaPO po) {
        int updated = mapper.update(po);
        if (updated == 1) {
            deleteCachedTea(po.getTenantCode(), po.getTeaCode(), null);
            deleteCachedTea(po.getTenantCode(), null, po.getTeaName());
        }
        return updated;
    }

    public int delete(String tenantCode, String teaCode) {
        TeaPO po = selectOneByCode(tenantCode, teaCode);
        int deleted = mapper.delete(tenantCode, teaCode);
        if (deleted == 1) {
            // TODO 需要想办法删除用name缓存的对象
            deleteCachedTea(tenantCode, teaCode, po.getTeaName());
        }
        return deleted;
    }

    private String getCacheKey(String tenantCode, String teaCode, String teaName) {
        return "tea_acc_" + tenantCode + "-" + teaCode + "-" + teaName;
    }

    private String getCacheKey(String tenantCode) {
        return "tea_acc_" + tenantCode;
    }

    private TeaPO getCachedTea(String tenantCode, String teaCode, String teaName) {
        String key = getCacheKey(tenantCode, teaCode, teaName);
        Object cached = redisManager.getValue(key);
        TeaPO po = (TeaPO) cached;
        return po;
    }

    private List<TeaPO> getCachedTeaList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<TeaPO> poList = (List<TeaPO>) cached;
        return poList;
    }

    private void setCachedTeaList(String tenantCode, List<TeaPO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedTea(String tenantCode, String teaCode, String teaName, TeaPO po) {
        String key = getCacheKey(tenantCode, teaCode, teaName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedTea(String tenantCode, String teaCode, String teaName) {
        redisManager.deleteKey(getCacheKey(tenantCode, teaCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, teaName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
