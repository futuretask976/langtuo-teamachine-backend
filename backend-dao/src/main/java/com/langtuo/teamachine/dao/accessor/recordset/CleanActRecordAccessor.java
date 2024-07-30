package com.langtuo.teamachine.dao.accessor.recordset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.recordset.CleanActRecordMapper;
import com.langtuo.teamachine.dao.po.recordset.CleanActRecordPO;
import com.langtuo.teamachine.dao.query.recordset.CleanActRecordQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class CleanActRecordAccessor {
    @Resource
    private CleanActRecordMapper mapper;

    public CleanActRecordPO selectOne(String tenantCode, String idempotentMark) {
        return mapper.selectOne(tenantCode, idempotentMark);
    }

    public List<CleanActRecordPO> selectList(String tenantCode) {
        List<CleanActRecordPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<CleanActRecordPO> search(String tenantCode, String shopGroupCode, String shopCode,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        CleanActRecordQuery query = new CleanActRecordQuery();
        query.setTenantCode(tenantCode);
        query.setShopGroupCode(StringUtils.isBlank(shopGroupCode) ? null : shopGroupCode);
        query.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        List<CleanActRecordPO> list = mapper.search(query);

        PageInfo<CleanActRecordPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(CleanActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
