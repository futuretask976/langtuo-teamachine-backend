package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MachinePO;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MachineMapper {
    /**
     *
     * @param tenantCode
     * @param machineCode
     * @return
     */
    MachinePO selectOne(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode);

    /**
     *
     * @return
     */
    List<MachinePO> selectList();

    /**
     *
     * @param machinePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MachinePO machinePO);

    /**
     *
     * @param machinePO
     * @return
     */
    int update(MachinePO machinePO);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode);
}
