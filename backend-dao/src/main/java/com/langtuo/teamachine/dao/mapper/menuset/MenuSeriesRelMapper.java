package com.langtuo.teamachine.dao.mapper.menuset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.menuset.MenuSeriesRelPO;
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
    MenuSeriesRelPO selectOne(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode,
            @Param("seriesCode") String seriesCode);

    /**
     *
     * @return
     */
    List<MenuSeriesRelPO> selectList(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);

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
     * @param menuCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);
}
