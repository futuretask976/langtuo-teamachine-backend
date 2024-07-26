package com.langtuo.teamachine.dao.mapper.deviceset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.deviceset.ModelPO;
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
    ModelPO selectOne(@Param("modelCode") String modelCode);

    /**
     *
     * @return
     */
    List<ModelPO> selectList();

    /**
     *
     * @return
     */
    List<ModelPO> search(MachineModelQuery machineModelQuery);

    /**
     *
     * @param modelPO
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(ModelPO modelPO);

    /**
     *
     * @param modelPO
     * @return
     */
    int update(ModelPO modelPO);

    /**
     *
     * @param machineCode
     * @return
     */
    int delete(String machineCode);
}
