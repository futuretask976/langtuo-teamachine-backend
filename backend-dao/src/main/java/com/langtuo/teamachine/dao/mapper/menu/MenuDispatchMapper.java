package com.langtuo.teamachine.dao.mapper.menu;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.menu.MenuDispatchPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface MenuDispatchMapper {
    /**
     *
     * @param tenantCode
     * @param menuCode
     * @param shopGroupCode
     * @return
     */
    MenuDispatchPO selectOne(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @return
     */
    List<MenuDispatchPO> selectList(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);

    /**
     *
     * @return
     */
    List<MenuDispatchPO> selectListByShopGroupCode(@Param("tenantCode") String tenantCode,
            @Param("shopGroupCode") String shopGroupCode);

    /**
     *
     * @param menuDispatchPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);
}
