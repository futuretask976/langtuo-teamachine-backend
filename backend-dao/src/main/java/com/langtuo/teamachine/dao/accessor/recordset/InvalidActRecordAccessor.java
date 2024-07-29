package com.langtuo.teamachine.dao.accessor.recordset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.recordset.InvalidActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.InvalidActRecordPO;
import com.langtuo.teamachine.dao.query.recordset.InvalidActRecordQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class InvalidActRecordAccessor {
    @Resource
    private InvalidActRecordMapper mapper;

    public InvalidActRecordPO selectOne(String tenantCode, String idempotentMark) {
        return mapper.selectOne(tenantCode, idempotentMark);
    }

    public List<InvalidActRecordPO> selectList(String tenantCode) {
        List<InvalidActRecordPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<InvalidActRecordPO> search(String tenantCode, String shopGroupCode, String shopCode,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        InvalidActRecordQuery query = new InvalidActRecordQuery();
        query.setTenantCode(tenantCode);
        query.setShopGroupCode(StringUtils.isBlank(shopGroupCode) ? null : shopGroupCode);
        query.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        List<InvalidActRecordPO> list = mapper.search(query);

        PageInfo<InvalidActRecordPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(InvalidActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
