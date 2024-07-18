package com.langtuo.teamachine.dao.accessor.menuset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.menuset.MenuDispatchMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuDispatchPO;
import com.langtuo.teamachine.dao.query.menuset.MenuQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuDispatchAccessor {
    @Resource
    private MenuDispatchMapper mapper;

    public MenuDispatchPO selectOneByCode(String tenantCode, String menuCode) {
        return mapper.selectOne(tenantCode, menuCode, null);
    }

    public MenuDispatchPO selectOneByName(String tenantCode, String menuName) {
        return mapper.selectOne(tenantCode, null, menuName);
    }

    public List<MenuDispatchPO> selectList(String tenantCode) {
        List<MenuDispatchPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public int insert(MenuDispatchPO po) {
        return mapper.insert(po);
    }

    public int update(MenuDispatchPO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String menuCode) {
        return mapper.delete(tenantCode, menuCode);
    }
}
