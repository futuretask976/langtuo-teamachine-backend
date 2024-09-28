package com.langtuo.teamachine.dao.mapper.report;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.report.OrderReportPO;
import com.langtuo.teamachine.dao.query.report.OrderReportQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderReportMapper {
    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    List<OrderReportPO> calcByDay(@Param("tenantCode") String tenantCode, @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @return
     */
    List<OrderReportPO> search(OrderReportQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderReportPO po);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orderCreatedDay") String orderCreatedDay);
}
