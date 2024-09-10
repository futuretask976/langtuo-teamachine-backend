package com.langtuo.teamachine.dao.mapper.report;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.report.OrderAmtReportPO;
import com.langtuo.teamachine.dao.query.report.OrderAmtReportQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderAmtReportMapper {
    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    OrderAmtReportPO calcOne(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    OrderAmtReportPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @return
     */
    List<OrderAmtReportPO> search(OrderAmtReportQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderAmtReportPO po);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orderCreatedDay") String orderCreatedDay);
}
