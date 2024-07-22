package com.langtuo.teamachine.dao.mapper.deviceset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.deviceset.MachineModelPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineModelQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface MachineModelMapper {
    /**
     *
     * @param modelCode
     * @return
     */
    MachineModelPO selectOne(@Param("modelCode") String modelCode);

    /**
     *
     * @return
     */
    List<MachineModelPO> selectList();

    /**
     *
     * @return
     */
    List<MachineModelPO> search(MachineModelQuery machineModelQuery);

    /**
     *
     * @param machineModelPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MachineModelPO machineModelPO);

    /**
     *
     * @param machineModelPO
     * @return
     */
    int update(MachineModelPO machineModelPO);

    /**
     *
     * @param machineCode
     * @return
     */
    int delete(String machineCode);
}
