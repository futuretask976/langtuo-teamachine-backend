package com.langtuo.teamachine.dao.mapper.deviceset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.deviceset.MachineDeployPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineDeployQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
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
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
