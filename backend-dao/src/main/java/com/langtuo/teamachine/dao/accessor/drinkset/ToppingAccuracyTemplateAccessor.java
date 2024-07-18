package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingAccuracyTemplateMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTemplatePO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingAccuracyTemplateQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAccuracyTemplateAccessor {
    @Resource
    private ToppingAccuracyTemplateMapper mapper;

    public ToppingAccuracyTemplatePO selectOneByCode(String tenantCode, String templateCode) {
        return mapper.selectOne(tenantCode, templateCode, null);
    }

    public ToppingAccuracyTemplatePO selectOneByName(String tenantCode, String templateName) {
        return mapper.selectOne(tenantCode, null, templateName);
    }

    public List<ToppingAccuracyTemplatePO> selectList(String tenantCode) {
        List<ToppingAccuracyTemplatePO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<ToppingAccuracyTemplatePO> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingAccuracyTemplateQuery query = new ToppingAccuracyTemplateQuery();
        query.setTenantCode(tenantCode);
        query.setTemplateCode(StringUtils.isBlank(templateCode) ? null : templateCode);
        query.setTemplateName(StringUtils.isBlank(templateName) ? null : templateName);
        List<ToppingAccuracyTemplatePO> list = mapper.search(query);

        PageInfo<ToppingAccuracyTemplatePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingAccuracyTemplatePO po) {
        return mapper.insert(po);
    }

    public int update(ToppingAccuracyTemplatePO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String toppingCode) {
        return mapper.delete(tenantCode, toppingCode);
    }
}
