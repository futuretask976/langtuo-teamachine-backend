package com.langtuo.teamachine.dao.accessor.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.report.OrderToppingReportByShopMapper;
import com.langtuo.teamachine.dao.po.report.OrderToppingReportByShopPO;
import com.langtuo.teamachine.dao.query.report.OrderToppingReportByShopQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderToppingReportByShopAccessor {
    @Resource
    private OrderToppingReportByShopMapper mapper;

    public List<OrderToppingReportByShopPO> calcByDay(String tenantCode, String orderCreatedDay) {
        return mapper.calcByDay(tenantCode, orderCreatedDay);
    }

    public List<OrderToppingReportByShopPO> selectListByDay(String tenantCode, String orderCreatedDay) {
        return mapper.selectListByDay(tenantCode, orderCreatedDay);
    }

    public PageInfo<OrderToppingReportByShopPO> searchByShopGroupCode(String tenantCode,
            List<String> orderCreatedDayList, List<String> shopGroupCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderToppingReportByShopQuery query = new OrderToppingReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopGroupCode(shopGroupCodeList);
        List<OrderToppingReportByShopPO> list = mapper.search(query);

        PageInfo<OrderToppingReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<OrderToppingReportByShopPO> searchByShopCode(String tenantCode, List<String> orderCreatedDayList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderToppingReportByShopQuery query = new OrderToppingReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopCode(shopCodeList);
        List<OrderToppingReportByShopPO> list = mapper.search(query);

        PageInfo<OrderToppingReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrderToppingReportByShopPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String orderCreatedDay) {
        return mapper.delete(tenantCode, orderCreatedDay);
    }
}
