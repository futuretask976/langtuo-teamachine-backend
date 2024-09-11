package com.langtuo.teamachine.web.controller.report;

import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.report.OrderAmtReportDTO;
import com.langtuo.teamachine.api.model.report.OrderSpecItemReportByShopDTO;
import com.langtuo.teamachine.api.model.report.OrderTeaReportByShopDTO;
import com.langtuo.teamachine.api.model.report.OrderToppingReportByShopDTO;
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

    @GetMapping(value = "/teareportbyshop/search")
    public TeaMachineResult<PageDTO<OrderTeaReportByShopDTO>> searchTeaReportByShopReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderTeaReportByShopDTO>> rtn = orderReportService.searchTeaReportByShop(tenantCode,
                orderCreatedDay, shopGroupCode, shopCode, pageNum, pageSize);
        return rtn;
    }

    @GetMapping(value = "/specitemreportbyshop/search")
    public TeaMachineResult<PageDTO<OrderSpecItemReportByShopDTO>> searchSpecItemReportByShopReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderSpecItemReportByShopDTO>> rtn =
                orderReportService.searchSpecItemReportByShop(tenantCode, orderCreatedDay, shopGroupCode, shopCode,
                        pageNum, pageSize);
        return rtn;
    }

    @GetMapping(value = "/toppingreportbyshop/search")
    public TeaMachineResult<PageDTO<OrderToppingReportByShopDTO>> searchToppingReportByShopReport(
            @RequestParam(name = "tenantCode") String tenantCode,
            @RequestParam(name = "orderCreatedDay") String orderCreatedDay,
            @RequestParam(name = "shopGroupCode") String shopGroupCode,
            @RequestParam(name = "shopCode") String shopCode,
            @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
        TeaMachineResult<PageDTO<OrderToppingReportByShopDTO>> rtn =
                orderReportService.searchToppingReportByShop(tenantCode, orderCreatedDay, shopGroupCode, shopCode,
                        pageNum, pageSize);
        return rtn;
    }
}
