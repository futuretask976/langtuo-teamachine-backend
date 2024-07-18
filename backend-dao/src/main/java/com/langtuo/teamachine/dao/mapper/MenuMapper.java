package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MenuPO;
import com.langtuo.teamachine.dao.query.MenuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MenuMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    MenuPO selectOne(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode,
            @Param("menuName") String menuName);

    /**
     *
     * @return
     */
    List<MenuPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<MenuPO> search(MenuQuery query);

    /**
     *
     * @param menuPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MenuPO menuPO);

    /**
     *
     * @param menuPO
     * @return
     */
    int update(MenuPO menuPO);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);
}
