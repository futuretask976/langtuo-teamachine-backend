package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MachineDeployPO;
import com.langtuo.teamachine.dao.po.MachinePO;
import com.langtuo.teamachine.dao.po.ShopPO;
import com.langtuo.teamachine.dao.query.MachineDeployQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MachineDeployMapper {
    /**
     *
     * @param tenantCode
     * @param deployCode
     * @return
     */
    MachineDeployPO selectOne(@Param("tenantCode") String tenantCode, @Param("deployCode") String deployCode);

    /**
     *
     * @return
     */
    List<MachineDeployPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<MachineDeployPO> search(MachineDeployQuery query);

    /**
     *
     * @param machineDeployPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MachineDeployPO machineDeployPO);

    /**
     *
     * @param machineDeployPO
     * @return
     */
    int update(MachineDeployPO machineDeployPO);

    /**
     * 
     * @param tenantCode
     * @param deployCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("deployCode") String deployCode);
}
