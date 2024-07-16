package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.SeriesMapper;
import com.langtuo.teamachine.dao.po.SeriesPO;
import com.langtuo.teamachine.dao.query.SeriesQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SeriesAccessor {
    @Resource
    private SeriesMapper mapper;

    public SeriesPO selectOneByCode(String tenantCode, String seriesCode) {
        return mapper.selectOne(tenantCode, seriesCode, null);
    }

    public SeriesPO selectOneByName(String tenantCode, String seriesName) {
        return mapper.selectOne(tenantCode, null, seriesName);
    }

    public List<SeriesPO> selectList(String tenantCode) {
        List<SeriesPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<SeriesPO> search(String tenantCode, String seriesCode, String seriesName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        SeriesQuery query = new SeriesQuery();
        query.setTenantCode(tenantCode);
        query.setSeriesName(StringUtils.isBlank(seriesName) ? null : seriesName);
        query.setSeriesCode(StringUtils.isBlank(seriesCode) ? null : seriesCode);
        List<SeriesPO> list = mapper.search(query);

        PageInfo<SeriesPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(SeriesPO seriesPO) {
        return mapper.insert(seriesPO);
    }

    public int update(SeriesPO seriesPO) {
        return mapper.update(seriesPO);
    }

    public int delete(String tenantCode, String seriesCode) {
        return mapper.delete(tenantCode, seriesCode);
    }
}
