package com.gx.sp3.demo.dao.mapper.langtuo;

import com.gx.sp3.demo.dao.annotation.GxTableShard;
import com.gx.sp3.demo.dao.annotation.MySQLScan;
import com.gx.sp3.demo.dao.pojo.langtuo.MachinePojo;
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
