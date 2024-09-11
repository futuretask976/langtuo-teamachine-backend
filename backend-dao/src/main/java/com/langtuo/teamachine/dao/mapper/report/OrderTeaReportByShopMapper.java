package com.langtuo.teamachine.dao.mapper.report;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
import com.langtuo.teamachine.dao.po.report.OrderAmtReportPO;
import com.langtuo.teamachine.dao.po.report.OrderSpecItemReportByShopPO;
import com.langtuo.teamachine.dao.po.report.OrderTeaReportByShopPO;
import com.langtuo.teamachine.dao.query.record.SupplyActRecordQuery;
import com.langtuo.teamachine.dao.query.report.OrderSpecItemReportByShopQuery;
import com.langtuo.teamachine.dao.query.report.OrderTeaReportByShopQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@TeaMachineSQLScan
@Repository
public interface OrderTeaReportByShopMapper {
    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    List<OrderTeaReportByShopPO> calcByDay(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    List<OrderTeaReportByShopPO> selectListByDay(@Param("tenantCode") String tenantCode,
            @Param("orderCreatedDay") String orderCreatedDay);

    /**
     *
     * @return
     */
    List<OrderTeaReportByShopPO> search(OrderTeaReportByShopQuery query);

    /**
     *
     * @param po
     * @return
     */
    @TeaMachineTableShard(tableShardOpen = true, shardName = "table_new", columns = "version", defaultName = "table")
    int insert(OrderTeaReportByShopPO po);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    int delete(@Param("tenantCode") String tenantCode, @Param("orderCreatedDay") String orderCreatedDay);
}
