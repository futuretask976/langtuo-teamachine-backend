package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.TeaMapper;
import com.langtuo.teamachine.dao.po.drinkset.TeaPO;
import com.langtuo.teamachine.dao.query.drinkset.TeaQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TeaAccessor {
    @Resource
    private TeaMapper mapper;

    public TeaPO selectOneByCode(String tenantCode, String teaCode) {
        return mapper.selectOne(tenantCode, teaCode, null);
    }

    public TeaPO selectOneByName(String tenantCode, String teaName) {
        return mapper.selectOne(tenantCode, null, teaName);
    }

    public List<TeaPO> selectList(String tenantCode) {
        List<TeaPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<TeaPO> search(String tenantCode, String teaCode, String teaName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TeaQuery query = new TeaQuery();
        query.setTenantCode(tenantCode);
        query.setTeaName(StringUtils.isBlank(teaName) ? null : teaName);
        query.setTeaCode(StringUtils.isBlank(teaCode) ? null : teaCode);
        List<TeaPO> list = mapper.search(query);

        PageInfo<TeaPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(TeaPO toppingTypePO) {
        return mapper.insert(toppingTypePO);
    }

    public int update(TeaPO toppingTypePO) {
        return mapper.update(toppingTypePO);
    }

    public int delete(String tenantCode, String teaCode) {
        return mapper.delete(tenantCode, teaCode);
    }
}
