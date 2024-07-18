package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaTypeMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaTypePO;
import com.langtuo.teamachine.dao.query.drinkset.TeaTypeQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaTypeAccessor {
    @Resource
    private TeaTypeMapper mapper;

    public TeaTypePO selectOneByCode(String tenantCode, String teaTypeCode) {
        return mapper.selectOne(tenantCode, teaTypeCode, null);
    }

    public TeaTypePO selectOneByName(String tenantCode, String teaTypeName) {
        return mapper.selectOne(tenantCode, null, teaTypeName);
    }

    public List<TeaTypePO> selectList(String tenantCode) {
        List<TeaTypePO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<TeaTypePO> search(String tenantCode, String teaTypeCode, String teaTypeName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaTypeQuery query = new TeaTypeQuery();
        query.setTenantCode(tenantCode);
        query.setTeaTypeName(StringUtils.isBlank(teaTypeName) ? null : teaTypeName);
        query.setTeaTypeCode(StringUtils.isBlank(teaTypeCode) ? null : teaTypeCode);
        List<TeaTypePO> list = mapper.search(query);

        PageInfo<TeaTypePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaTypePO toppingTypePO) {
        return mapper.insert(toppingTypePO);
    }

    public int update(TeaTypePO toppingTypePO) {
        return mapper.update(toppingTypePO);
    }

    public int delete(String tenantCode, String teaTypeCode) {
        return mapper.delete(tenantCode, teaTypeCode);
    }
}
