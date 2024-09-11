package com.langtuo.teamachine.dao.accessor.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.report.OrderTeaReportByShopMapper;
import com.langtuo.teamachine.dao.po.report.OrderTeaReportByShopPO;
import com.langtuo.teamachine.dao.query.report.OrderTeaReportByShopQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderTeaReportByShopAccessor {
    @Resource
    private OrderTeaReportByShopMapper mapper;

    public List<OrderTeaReportByShopPO> calcByDay(String tenantCode, String orderCreatedDay) {
        return mapper.calcByDay(tenantCode, orderCreatedDay);
    }

    public List<OrderTeaReportByShopPO> selectListByDay(String tenantCode, String orderCreatedDay) {
        return mapper.selectListByDay(tenantCode, orderCreatedDay);
    }

    public PageInfo<OrderTeaReportByShopPO> searchByShopGroupCode(String tenantCode, List<String> orderCreatedDayList,
            List<String> shopGroupCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderTeaReportByShopQuery query = new OrderTeaReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopGroupCode(shopGroupCodeList);
        List<OrderTeaReportByShopPO> list = mapper.search(query);

        PageInfo<OrderTeaReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<OrderTeaReportByShopPO> searchByShopCode(String tenantCode, List<String> orderCreatedDayList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderTeaReportByShopQuery query = new OrderTeaReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopCode(shopCodeList);
        List<OrderTeaReportByShopPO> list = mapper.search(query);

        PageInfo<OrderTeaReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrderTeaReportByShopPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String orderCreatedDay) {
        return mapper.delete(tenantCode, orderCreatedDay);
    }
}
