package com.langtuo.teamachine.dao.mapper.langtuo;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
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
     * @param machineCode
     * @return
     */
    MachinePojo get(@Param("machineCode") String machineCode);

    /**
     *
     * @return
     */
    List<MachinePojo> list();

    /**
     *
     * @param machinePojo
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(MachinePojo machinePojo);

    /**
     *
     * @param machinePojo
     * @return
     */
    int update(MachinePojo machinePojo);

    /**
     *
     * @param machineCode
     * @return
     */
    int delete(String machineCode);
}
