package com.langtuo.teamachine.dao.accessor.record;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.record.OrderActRecordMapper;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
import com.langtuo.teamachine.dao.query.record.OrderActRecordQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderActRecordAccessor {
    @Resource
    private OrderActRecordMapper mapper;

    public OrderActRecordPO selectOne(String tenantCode, String idempotentMark) {
        return mapper.selectOne(tenantCode, idempotentMark);
    }

    public PageInfo<OrderActRecordPO> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderActRecordQuery query = new OrderActRecordQuery();
        query.setTenantCode(tenantCode);
        query.addShopGroupCode(shopGroupCodeList);
        query.addShopCode(shopCodeList);
        List<OrderActRecordPO> list = mapper.search(query);

        PageInfo<OrderActRecordPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(OrderActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
