package com.langtuo.teamachine.dao.accessor.record;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.record.DrainActRecordMapper;
import com.langtuo.teamachine.dao.po.record.DrainActRecordPO;
import com.langtuo.teamachine.dao.query.record.DrainActRecordQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DrainActRecordAccessor {
    @Resource
    private DrainActRecordMapper mapper;

    public DrainActRecordPO selectOne(String tenantCode, String idempotentMark) {
        return mapper.selectOne(tenantCode, idempotentMark);
    }

    public PageInfo<DrainActRecordPO> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        DrainActRecordQuery query = new DrainActRecordQuery();
        query.setTenantCode(tenantCode);
        query.addShopCode(shopCodeList);
        List<DrainActRecordPO> list = mapper.search(query);

        PageInfo<DrainActRecordPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(DrainActRecordPO po) {
        return mapper.insert(po);
    }

    public int delete(String tenantCode, String idempotentMark) {
        return mapper.delete(tenantCode, idempotentMark);
    }
}
