package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MenuDispatchPO;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MenuDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param menuCode
     * @param shopCode
     * @return
     */
    MenuDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode, @Param("shopCode") String shopCode);

    /**
     *
     * @return
     */
    List<MenuDispatchPO> selectList();

    /**
     *
     * @param menuDispatchPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MenuDispatchPO menuDispatchPO);

    /**
     *
     * @param menuDispatchPO
     * @return
     */
    int update(MenuDispatchPO menuDispatchPO);

    /**
     *
     * @param tenantCode
     * @param menuCode
     * @param shopCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode, @Param("shopCode") String shopCode);
}
