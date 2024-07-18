package com.langtuo.teamachine.dao.mapper.reportset;

import com.langtuo.teamachine.dao.annotation.GxTableShard;
import com.langtuo.teamachine.dao.annotation.MySQLScan;
import com.langtuo.teamachine.dao.po.reportset.OrderActRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@MySQLScan
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
     * @param orderActRecordPO
     * @return
     */
    @GxTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderActRecordPO orderActRecordPO);

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
