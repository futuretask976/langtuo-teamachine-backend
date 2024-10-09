package com.langtuo.teamachine.dao.accessor.record;

import com.langtuo.teamachine.dao.mapper.record.OrderSpecItemActRecordMapper;
import com.langtuo.teamachine.dao.po.record.OrderSpecItemActRecordPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OrderSpecItemActRecordAccessor {
    @Resource
    private OrderSpecItemActRecordMapper mapper;

    public List<OrderSpecItemActRecordPO> listByIdempotentMark(String tenantCode, String shopGroupCode,
            String idempotentMark) {
        List<OrderSpecItemActRecordPO> list = mapper.selectList(tenantCode, shopGroupCode, idempotentMark);
        return list;
    }

    public int insert(OrderSpecItemActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String shopGroupCode, String idempotentMark) {
        return mapper.delete(tenantCode, shopGroupCode, idempotentMark);
    }
}
