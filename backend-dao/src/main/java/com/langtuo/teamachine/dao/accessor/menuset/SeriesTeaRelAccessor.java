package com.langtuo.teamachine.dao.accessor.menuset;

import com.langtuo.teamachine.dao.mapper.menuset.SeriesTeaRelMapper;
import com.langtuo.teamachine.dao.po.menuset.SeriesTeaRelPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeriesTeaRelAccessor {
    @Resource
    private SeriesTeaRelMapper mapper;

    public SeriesTeaRelPO selectOne(String tenantCode, String seriesCode, String teaCode) {
        return mapper.selectOne(tenantCode, seriesCode, teaCode);
    }

    public List<SeriesTeaRelPO> selectList(String tenantCode, String seriesCode) {
        List<SeriesTeaRelPO> list = mapper.selectList(tenantCode, seriesCode);
        return list;
    }

    public int insert(SeriesTeaRelPO seriesPO) {
        return mapper.insert(seriesPO);
    }

    public int delete(String tenantCode, String seriesCode) {
        return mapper.delete(tenantCode, seriesCode);
    }
}
