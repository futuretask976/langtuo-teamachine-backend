package com.langtuo.teamachine.dao.mapper.device;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.query.device.MachineDeployQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface DeployMapper {
    /**
     *
     * @param deployCode
     * @return
     */
    DeployPO selectOne(@Param("tenantCode") String tenantCode, @Param("deployCode") String deployCode,
            @Param("machineCode") String machineCode);

    /**
     *
     * @return
     */
    List<DeployPO> selectList(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<DeployPO> search(MachineDeployQuery query);

    /**
     *
     * @param deployPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(DeployPO deployPO);

    /**
     *
     * @param deployPO
     * @return
     */
    int update(DeployPO deployPO);

    /**
     * 
     * @param tenantCode
     * @param deployCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("deployCode") String deployCode);
}
