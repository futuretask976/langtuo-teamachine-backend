package com.langtuo.teamachine.api.service.report;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.report.OrderAmtReportDTO;
import com.langtuo.teamachine.api.model.report.OrderSpecItemReportDTO;
import com.langtuo.teamachine.api.model.report.OrderTeaReportDTO;
import com.langtuo.teamachine.api.model.report.OrderToppingReportDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;

public interface OrderReportService {
    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @return
     */
    TeaMachineResult<Void> calc(String tenantCode, String orderCreatedDay);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @param pageNum
     * @param pageSize
     * @return
     */
    TeaMachineResult<PageDTO<OrderAmtReportDTO>> searchAmtReport(String tenantCode, String orderCreatedDay,
            int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @param shopGroupCode
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    TeaMachineResult<PageDTO<OrderTeaReportDTO>> searchTeaReport(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @param shopGroupCode
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    TeaMachineResult<PageDTO<OrderSpecItemReportDTO>> searchSpecItemReport(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize);

    /**
     *
     * @param tenantCode
     * @param orderCreatedDay
     * @param shopGroupCode
     * @param shopCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    TeaMachineResult<PageDTO<OrderToppingReportDTO>> searchToppingReport(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize);
}
