package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaTypeMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaTypePO;
import com.langtuo.teamachine.dao.query.drinkset.TeaTypeQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaTypeAccessor {
    @Resource
    private TeaTypeMapper mapper;

    @Resource
    private RedisManager redisManager;

    public TeaTypePO selectOneByCode(String tenantCode, String teaTypeCode) {
        // 首先访问缓存
        TeaTypePO cached = getCachedTea(tenantCode, teaTypeCode, null);
        if (cached != null) {
            return cached;
        }

        TeaTypePO po = mapper.selectOne(tenantCode, teaTypeCode, null);

        // 设置缓存
        setCachedTea(tenantCode, teaTypeCode, null, po);
        return po;
    }

    public TeaTypePO selectOneByName(String tenantCode, String teaName) {
        // 首先访问缓存
        TeaTypePO cached = getCachedTea(tenantCode, null, teaName);
        if (cached != null) {
            return cached;
        }

        TeaTypePO po = mapper.selectOne(tenantCode, null, teaName);

        // 设置缓存
        setCachedTea(tenantCode, null, teaName, po);
        return po;
    }

    public List<TeaTypePO> selectList(String tenantCode) {
        // 首先访问缓存
        List<TeaTypePO> cachedList = getCachedTeaList(tenantCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<TeaTypePO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<TeaTypePO> search(String tenantCode, String teaTypeCode, String teaName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaTypeQuery query = new TeaTypeQuery();
        query.setTenantCode(tenantCode);
        query.setTeaTypeName(StringUtils.isBlank(teaName) ? null : teaName);
        query.setTeaTypeCode(StringUtils.isBlank(teaTypeCode) ? null : teaTypeCode);
        List<TeaTypePO> list = mapper.search(query);

        PageInfo<TeaTypePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaTypePO po) {
        return mapper.insert(po);
    }

    public int update(TeaTypePO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String teaTypeCode) {
        return mapper.delete(tenantCode, teaTypeCode);
    }

    private String getCacheKey(String tenantCode, String teaTypeCode, String teaName) {
        return "tea_type_acc_" + tenantCode + "-" + teaTypeCode + "-" + teaName;
    }

    private String getCacheKey(String tenantCode) {
        return "tea_type_acc_" + tenantCode;
    }

    private TeaTypePO getCachedTea(String tenantCode, String teaTypeCode, String teaName) {
        String key = getCacheKey(tenantCode, teaTypeCode, teaName);
        Object cached = redisManager.getValue(key);
        TeaTypePO po = (TeaTypePO) cached;
        return po;
    }

    private List<TeaTypePO> getCachedTeaList(String tenantCode) {
        String key = getCacheKey(tenantCode);
        Object cached = redisManager.getValue(key);
        List<TeaTypePO> poList = (List<TeaTypePO>) cached;
        return poList;
    }

    private void setCachedTeaList(String tenantCode, List<TeaTypePO> poList) {
        String key = getCacheKey(tenantCode);
        redisManager.setValue(key, poList);
    }

    private void setCachedTea(String tenantCode, String teaTypeCode, String teaName, TeaTypePO po) {
        String key = getCacheKey(tenantCode, teaTypeCode, teaName);
        redisManager.setValue(key, po);
    }

    private void deleteCachedTea(String tenantCode, String teaTypeCode, String teaName) {
        redisManager.deleteKey(getCacheKey(tenantCode, teaTypeCode, null));
        redisManager.deleteKey(getCacheKey(tenantCode, null, teaName));
        redisManager.deleteKey(getCacheKey(tenantCode));
    }
}
