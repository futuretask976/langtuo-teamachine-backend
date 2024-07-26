package com.langtuo.teamachine.dao.mapper.deviceset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.deviceset.ModelPipelinePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface ModelPipelineMapper {
    /**
     *
     * @param modelCode
     * @param pipelineNum
     * @return
     */
    ModelPipelinePO selectOne(@Param("modelCode") String modelCode, @Param("pipelineNum") String pipelineNum);

    /**
     *
     * @return
     */
    List<ModelPipelinePO> selectList(String modelCode);

    /**
     *
     * @param modelPipelinePO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ModelPipelinePO modelPipelinePO);

    /**
     *
     * @param modelPipelinePO
     * @return
     */
    int update(ModelPipelinePO modelPipelinePO);

    /**
     *
     * @param modelCode
     * @return
     */
    int delete(@Param("modelCode") String modelCode);
}
