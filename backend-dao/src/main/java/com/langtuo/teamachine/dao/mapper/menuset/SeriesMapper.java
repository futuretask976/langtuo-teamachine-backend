package com.langtuo.teamachine.dao.mapper.menuset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.menuset.SeriesPO;
import com.langtuo.teamachine.dao.query.menuset.SeriesQuery;
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
    SeriesPO selectOne(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode,
            @Param("seriesName") String seriesName);

    /**
     *
     * @return
     */
    List<SeriesPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<SeriesPO> search(SeriesQuery query);

    /**
     *
     * @param po
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(SeriesPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(SeriesPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode);
}
