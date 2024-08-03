package com.langtuo.teamachine.dao.accessor.userset;

import com.langtuo.teamachine.dao.cache.RedisManager;
import com.langtuo.teamachine.dao.mapper.userset.RoleActRelMapper;
import com.langtuo.teamachine.dao.po.userset.RoleActRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RoleActRelAccessor {
    @Resource
    private RoleActRelMapper mapper;

    @Resource
    private RedisManager redisManager;

    public List<RoleActRelPO> selectList(String tenantCode, String roleCode) {
        // 首先访问缓存
        List<RoleActRelPO> cachedList = getCacheList(tenantCode, roleCode);
        if (cachedList != null) {
            return cachedList;
        }

        List<RoleActRelPO> list = mapper.selectList(tenantCode, roleCode);

        // 设置缓存
        setCacheList(tenantCode, roleCode, list);
        return list;
    }

    public int insert(RoleActRelPO roleActRelPO) {
        return mapper.insert(roleActRelPO);
    }

    public int delete(String tenantCode, String roleCode) {
        int deleted = mapper.delete(tenantCode, roleCode);
        if (deleted > 0) {
            deleteCacheAll(tenantCode, roleCode);
        }
        return deleted;
    }

    private String getCacheListKey(String tenantCode, String roleCode) {
        return "role_act_rel_acc_" + tenantCode + "-" + roleCode;
    }

    private List<RoleActRelPO> getCacheList(String tenantCode, String roleCode) {
        String key = getCacheListKey(tenantCode, roleCode);
        Object cached = redisManager.getValue(key);
        List<RoleActRelPO> poList = (List<RoleActRelPO>) cached;
        return poList;
    }

    private void setCacheList(String tenantCode, String roleCode, List<RoleActRelPO> poList) {
        String key = getCacheListKey(tenantCode, roleCode);
        redisManager.setValue(key, poList);
    }

    private void deleteCacheAll(String tenantCode, String roleCode) {
        redisManager.deleteKey(getCacheListKey(tenantCode, roleCode));
    }
}
