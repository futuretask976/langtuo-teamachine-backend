package com.langtuo.teamachine.web.controller.report;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.report.OrderAmtReportDTO;
import com.langtuo.teamachine.api.model.report.OrderSpecItemReportDTO;
import com.langtuo.teamachine.api.model.report.OrderTeaReportDTO;
import com.langtuo.teamachine.api.model.report.OrderToppingReportDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.report.OrderReportService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/reportset/order")
public class OrderReportController {
    @Resource
    private OrderReportService orderReportService;

    @GetMapping(value = "/calc")
    public TeaMachineResult<Void> calc(@RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay) {
        TeaMachineResult<Void> rtn = orderReportService.calc(tenantCode, orderCreatedDay);
        return rtn;
    }

    @GetMapping(value = "/amtreport/search")
    public TeaMachineResult<PageDTO<OrderAmtReportDTO>> searchAmtReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderAmtReportDTO>> rtn = orderReportService.searchAmtReport(tenantCode,
                orderCreatedDay, pageNum, pageSize);
        return rtn;
    }

    @GetMapping(value = "/teareport/search")
    public TeaMachineResult<PageDTO<OrderTeaReportDTO>> searchTeaReportReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderTeaReportDTO>> rtn = orderReportService.searchTeaReport(tenantCode,
                orderCreatedDay, shopGroupCode, shopCode, pageNum, pageSize);
        return rtn;
    }

    @GetMapping(value = "/specitemreport/search")
    public TeaMachineResult<PageDTO<OrderSpecItemReportDTO>> searchSpecItemReportReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderSpecItemReportDTO>> rtn =
                orderReportService.searchSpecItemReport(tenantCode, orderCreatedDay, shopGroupCode, shopCode,
                        pageNum, pageSize);
        return rtn;
    }

    @GetMapping(value = "/toppingreport/search")
    public TeaMachineResult<PageDTO<OrderToppingReportDTO>> searchToppingReportReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderToppingReportDTO>> rtn =
                orderReportService.searchToppingReport(tenantCode, orderCreatedDay, shopGroupCode, shopCode,
                        pageNum, pageSize);
        return rtn;
    }
}
