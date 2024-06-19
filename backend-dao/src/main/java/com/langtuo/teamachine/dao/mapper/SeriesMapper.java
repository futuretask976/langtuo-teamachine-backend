package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.SeriesPO;
import com.langtuo.teamachine.dao.po.TenantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface SeriesMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    SeriesPO selectOne(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode);

    /**
     *
     * @return
     */
    List<SeriesPO> selectList();

    /**
     *
     * @param seriesPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SeriesPO seriesPO);

    /**
     *
     * @param seriesPO
     * @return
     */
    int update(SeriesPO seriesPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode);
}
