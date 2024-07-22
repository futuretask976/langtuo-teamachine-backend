package com.langtuo.teamachine.dao.mapper.menuset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.menuset.MenuPO;
import com.langtuo.teamachine.dao.query.menuset.MenuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
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
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MenuPO po);

    /**
     *
     * @param po
     * @return
     */
    int update(MenuPO po);

    /**
     *
     * @param tenantCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("menuCode") String menuCode);
}
