package com.langtuo.teamachine.dao.mapper.menuset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.menuset.SeriesTeaRelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface SeriesTeaRelMapper {
    /**
     *
     * @param tenantCode
     * @param teaCode
     * @param seriesCode
     * @return
     */
    SeriesTeaRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode,
            @Param("teaCode") String teaCode);

    /**
     *
     * @return
     */
    List<SeriesTeaRelPO> selectList(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode);

    /**
     *
     * @param menuSeriesRelPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SeriesTeaRelPO menuSeriesRelPO);

    /**
     *
     * @param tenantCode
     * @param seriesCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode);
}
