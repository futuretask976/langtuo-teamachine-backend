package com.langtuo.teamachine.dao.accessor.menuset;

import com.langtuo.teamachine.dao.mapper.menuset.MenuSeriesRelMapper;
import com.langtuo.teamachine.dao.po.menuset.MenuSeriesRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuSeriesRelAccessor {
    @Resource
    private MenuSeriesRelMapper mapper;

    public MenuSeriesRelPO selectOne(String tenantCode, String seriesCode, String teaCode) {
        return mapper.selectOne(tenantCode, seriesCode, teaCode);
    }

    public List<MenuSeriesRelPO> selectList(String tenantCode, String seriesCode) {
        List<MenuSeriesRelPO> list = mapper.selectList(tenantCode, seriesCode);
        return list;
    }

    public int insert(MenuSeriesRelPO menuSeriesRelPO) {
        return mapper.insert(menuSeriesRelPO);
    }

    public int delete(String tenantCode, String seriesCode) {
        return mapper.delete(tenantCode, seriesCode);
    }
}
