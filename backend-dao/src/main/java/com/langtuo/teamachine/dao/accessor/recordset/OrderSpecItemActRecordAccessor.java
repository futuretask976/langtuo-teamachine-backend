package com.langtuo.teamachine.dao.accessor.recordset;

import com.langtuo.teamachine.dao.mapper.recordset.OrderSpecItemActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.OrderSpecItemActRecordPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderSpecItemActRecordAccessor {
    @Resource
    private OrderSpecItemActRecordMapper mapper;

    public List<OrderSpecItemActRecordPO> selectList(String tenantCode, String idempotentMark) {
        List<OrderSpecItemActRecordPO> list = mapper.selectList(tenantCode, idempotentMark);
        return list;
    }

    public int insert(OrderSpecItemActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
