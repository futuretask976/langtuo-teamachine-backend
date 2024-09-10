package com.langtuo.teamachine.dao.mapper.report;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.report.OrderSpecItemReportByShopPO;
import com.langtuo.teamachine.dao.query.report.OrderSpecItemReportByShopQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderSpecItemReportByShopMapper {
    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    OrderSpecItemReportByShopPO calcOne(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    OrderSpecItemReportByShopPO selectOne(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @return
     */
    List<OrderSpecItemReportByShopPO> search(OrderSpecItemReportByShopQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderSpecItemReportByShopPO po);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orderCreatedDay") String orderCreatedDay);
}
