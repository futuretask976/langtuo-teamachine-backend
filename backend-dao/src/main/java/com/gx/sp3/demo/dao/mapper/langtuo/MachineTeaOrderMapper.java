package com.gx.sp3.demo.dao.mapper.langtuo;

import com.gx.sp3.demo.dao.annotation.MySQLScan;
import com.gx.sp3.demo.dao.annotation.GxTableShard;
import com.gx.sp3.demo.dao.pojo.langtuo.MachineTeaOrderPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
@Repository
public interface MachineTeaOrderMapper {
    /**
     *
     * @param orderId
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    MachineTeaOrderPojo get(@Param("orderId") String orderId);

    /**
     *
     * @return
     */
    List<MachineTeaOrderPojo> list();

    /**
     *
     * @param machineTeaOrderPojo
     * @return
     */
    int insert(MachineTeaOrderPojo machineTeaOrderPojo);

    /**
     *
     * @param machineTeaOrderPojo
     * @return
     */
    int update(MachineTeaOrderPojo machineTeaOrderPojo);

    /**
     *
     * @param orderId
     * @return
     */
    int delete(String orderId);
}
