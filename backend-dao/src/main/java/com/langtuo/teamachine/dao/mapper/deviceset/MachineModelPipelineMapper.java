package com.langtuo.teamachine.dao.mapper.deviceset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.deviceset.MachineModelPipelinePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MachineModelPipelineMapper {
    /**
     *
     * @param modelCode
     * @param pipelineNum
     * @return
     */
    MachineModelPipelinePO selectOne(@Param("modelCode") String modelCode, @Param("pipelineNum") String pipelineNum);

    /**
     *
     * @return
     */
    List<MachineModelPipelinePO> selectList(String modelCode);

    /**
     *
     * @param machineModelPipelinePO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MachineModelPipelinePO machineModelPipelinePO);

    /**
     *
     * @param machineModelPipelinePO
     * @return
     */
    int update(MachineModelPipelinePO machineModelPipelinePO);

    /**
     *
     * @param modelCode
     * @return
     */
    int delete(@Param("modelCode") String modelCode);
}
