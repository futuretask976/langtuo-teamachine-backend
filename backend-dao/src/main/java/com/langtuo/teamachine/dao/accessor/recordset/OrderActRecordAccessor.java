package com.langtuo.teamachine.dao.accessor.recordset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.recordset.OrderActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.OrderActRecordPO;
import com.langtuo.teamachine.dao.query.recordset.OrderActRecordQuery;
import org.apache.commons.lang3.StringUtils;
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

    public List<OrderActRecordPO> selectList(String tenantCode) {
        List<OrderActRecordPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<OrderActRecordPO> search(String tenantCode, String shopGroupCode, String shopCode,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        OrderActRecordQuery query = new OrderActRecordQuery();
        query.setTenantCode(tenantCode);
        query.setShopGroupCode(StringUtils.isBlank(shopGroupCode) ? null : shopGroupCode);
        query.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
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
