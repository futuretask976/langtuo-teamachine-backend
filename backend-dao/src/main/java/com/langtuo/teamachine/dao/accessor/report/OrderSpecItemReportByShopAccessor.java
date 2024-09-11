package com.langtuo.teamachine.dao.accessor.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.report.OrderSpecItemReportByShopMapper;
import com.langtuo.teamachine.dao.po.report.OrderSpecItemReportByShopPO;
import com.langtuo.teamachine.dao.query.report.OrderSpecItemReportByShopQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderSpecItemReportByShopAccessor {
    @Resource
    private OrderSpecItemReportByShopMapper mapper;

    public List<OrderSpecItemReportByShopPO> calcByDay(String tenantCode, String orderCreatedDay) {
        return mapper.calcByDay(tenantCode, orderCreatedDay);
    }

    public List<OrderSpecItemReportByShopPO> selectListByDay(String tenantCode, String orderCreatedDay) {
        return mapper.selectListByDay(tenantCode, orderCreatedDay);
    }

    public PageInfo<OrderSpecItemReportByShopPO> searchByShopGroupCode(String tenantCode, List<String> orderCreatedDayList,
            List<String> shopGroupCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderSpecItemReportByShopQuery query = new OrderSpecItemReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopGroupCode(shopGroupCodeList);
        List<OrderSpecItemReportByShopPO> list = mapper.search(query);

        PageInfo<OrderSpecItemReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<OrderSpecItemReportByShopPO> searchByShopCode(String tenantCode, List<String> orderCreatedDayList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderSpecItemReportByShopQuery query = new OrderSpecItemReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
        query.addAllShopCode(shopCodeList);
        List<OrderSpecItemReportByShopPO> list = mapper.search(query);

        PageInfo<OrderSpecItemReportByShopPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrderSpecItemReportByShopPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String orderCreatedDay) {
        return mapper.delete(tenantCode, orderCreatedDay);
    }
}
