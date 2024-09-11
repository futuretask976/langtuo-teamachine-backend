package com.langtuo.teamachine.dao.accessor.report;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.report.OrderAmtReportMapper;
import com.langtuo.teamachine.dao.po.report.OrderAmtReportPO;
import com.langtuo.teamachine.dao.query.report.OrderAmtReportQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderAmtReportAccessor {
    @Resource
    private OrderAmtReportMapper mapper;

    public OrderAmtReportPO calcOne(String tenantCode, String orderCreatedDay) {
        return mapper.calcOne(tenantCode, orderCreatedDay);
    }

    public OrderAmtReportPO selectOne(String tenantCode, String orderCreatedDay) {
        return mapper.selectOne(tenantCode, orderCreatedDay);
    }

    public PageInfo<OrderAmtReportPO> search(String tenantCode, List<String> orderCreatedDayList,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderAmtReportQuery query = new OrderAmtReportQuery();
        query.setTenantCode(tenantCode);
        query.addAllOrderCreatedDay(orderCreatedDayList);
        List<OrderAmtReportPO> list = mapper.search(query);

        PageInfo<OrderAmtReportPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrderAmtReportPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String orderCreatedDay) {
        return mapper.delete(tenantCode, orderCreatedDay);
    }
}
