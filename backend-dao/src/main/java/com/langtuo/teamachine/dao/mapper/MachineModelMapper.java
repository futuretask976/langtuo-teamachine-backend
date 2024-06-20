package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
     * @param machineModelPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
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
