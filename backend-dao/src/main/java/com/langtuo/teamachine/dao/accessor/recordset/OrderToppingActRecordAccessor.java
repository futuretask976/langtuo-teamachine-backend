package com.langtuo.teamachine.dao.accessor.recordset;

import com.langtuo.teamachine.dao.mapper.recordset.OrderToppingActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.OrderToppingActRecordPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderToppingActRecordAccessor {
    @Resource
    private OrderToppingActRecordMapper mapper;

    public List<OrderToppingActRecordPO> selectList(String tenantCode, String idempotentMark) {
        List<OrderToppingActRecordPO> list = mapper.selectList(tenantCode, idempotentMark);
        return list;
    }

    public int insert(OrderToppingActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
