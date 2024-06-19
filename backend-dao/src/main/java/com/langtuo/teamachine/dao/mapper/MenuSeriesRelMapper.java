package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MenuSeriesRelPO;
import com.langtuo.teamachine.dao.po.SeriesTeaRelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MenuSeriesRelMapper {
    /**
     *
     * @param tenantCode
     * @param seriesCode
     * @param menuCode
     * @return
     */
    MenuSeriesRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode, @Param("menuCode") String menuCode);

    /**
     *
     * @return
     */
    List<MenuSeriesRelPO> selectList();

    /**
     *
     * @param menuSeriesRelPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MenuSeriesRelPO menuSeriesRelPO);

    /**
     *
     * @param menuSeriesRelPO
     * @return
     */
    int update(MenuSeriesRelPO menuSeriesRelPO);

    /**
     *
     * @param tenantCode
     * @param seriesCode
     * @param menuCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("seriesCode") String seriesCode, @Param("menuCode") String menuCode);
}
