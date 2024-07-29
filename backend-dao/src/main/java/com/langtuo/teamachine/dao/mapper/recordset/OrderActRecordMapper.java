package com.langtuo.teamachine.dao.mapper.recordset;

import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.po.recordset.OrderActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderActRecordMapper {
    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param orderGmtCreated
     * @return
     */
    OrderActRecordPO selectOne(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
            @Param("shopCode") String shopCode, @Param("orderGmtCreated") String orderGmtCreated);

    /**
     *
     * @return
     */
    List<OrderActRecordPO> selectList();

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderActRecordPO po);

    /**
     *
     * @param tenantCode
     * @param machineCode
     * @param shopCode
     * @param orderGmtCreated
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("machineCode") String machineCode,
               @Param("shopCode") String shopCode, @Param("orderGmtCreated") String orderGmtCreated);
}
