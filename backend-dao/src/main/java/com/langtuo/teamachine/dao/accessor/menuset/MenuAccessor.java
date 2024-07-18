package com.langtuo.teamachine.dao.accessor.menuset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.menuset.MenuMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuPO;
import com.langtuo.teamachine.dao.query.menuset.MenuQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuAccessor {
    @Resource
    private MenuMapper mapper;

    public MenuPO selectOneByCode(String tenantCode, String menuCode) {
        return mapper.selectOne(tenantCode, menuCode, null);
    }

    public MenuPO selectOneByName(String tenantCode, String menuName) {
        return mapper.selectOne(tenantCode, null, menuName);
    }

    public List<MenuPO> selectList(String tenantCode) {
        List<MenuPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<MenuPO> search(String tenantCode, String menuCode, String menuName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MenuQuery query = new MenuQuery();
        query.setTenantCode(tenantCode);
        query.setMenuName(StringUtils.isBlank(menuName) ? null : menuName);
        query.setMenuCode(StringUtils.isBlank(menuCode) ? null : menuCode);
        List<MenuPO> list = mapper.search(query);

        PageInfo<MenuPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MenuPO seriesPO) {
        return mapper.insert(seriesPO);
    }

    public int update(MenuPO seriesPO) {
        return mapper.update(seriesPO);
    }

    public int delete(String tenantCode, String menuCode) {
        return mapper.delete(tenantCode, menuCode);
    }
}
