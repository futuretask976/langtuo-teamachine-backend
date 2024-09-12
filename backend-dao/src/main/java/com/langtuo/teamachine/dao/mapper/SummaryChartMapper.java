package com.langtuo.teamachine.dao.mapper;

import com.langtuo.teamachine.dao.annotation.TeaMachineSQLScan;
import com.langtuo.teamachine.dao.annotation.TeaMachineTableShard;
import com.langtuo.teamachine.dao.po.report.OrderAmtReportPO;
import com.langtuo.teamachine.dao.query.report.OrderAmtReportQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@TeaMachineSQLScan
@Repository
public interface SummaryChartMapper {
    /**
     *
     * @param tenantCode
     * @return
     */
    List<Map<String, String>> select4OrgShopChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    List<Map<String, String>> select4DeployChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @param tenantCode
     * @return
     */
    List<Map<String, String>> select4ShopMachineChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<Map<String, String>> select4OrderAmtChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<Map<String, String>> select4TeaAmtChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<Map<String, String>> select4ToppingAmtChart(@Param("tenantCode") String tenantCode);

    /**
     *
     * @return
     */
    List<Map<String, String>> select4SpecItemAmtChart(@Param("tenantCode") String tenantCode);
}
