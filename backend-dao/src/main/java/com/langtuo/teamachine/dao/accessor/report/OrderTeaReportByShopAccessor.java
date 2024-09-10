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

    public OrderTeaReportByShopPO calcOne(String tenantCode, String orderCreatedDay) {
        return mapper.selectOne(tenantCode, orderCreatedDay);
    }

    public OrderTeaReportByShopPO selectOne(String tenantCode, String orderCreatedDay) {
        return mapper.selectOne(tenantCode, orderCreatedDay);
    }

    public PageInfo<OrderTeaReportByShopPO> search(String tenantCode, List<String> orderCreatedDayList,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderTeaReportByShopQuery query = new OrderTeaReportByShopQuery();
        query.setTenantCode(tenantCode);
        query.addOrderCreatedDay(orderCreatedDayList);
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
