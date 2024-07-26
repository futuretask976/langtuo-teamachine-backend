package com.langtuo.teamachine.dao.accessor.drinkset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.drinkset.ToppingAccuracyTplMapper;
import com.langtuo.teamachine.dao.po.drinkset.ToppingAccuracyTplPO;
import com.langtuo.teamachine.dao.query.drinkset.ToppingAccuracyTplQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ToppingAccuracyTplAccessor {
    @Resource
    private ToppingAccuracyTplMapper mapper;

    public ToppingAccuracyTplPO selectOneByCode(String tenantCode, String templateCode) {
        return mapper.selectOne(tenantCode, templateCode, null);
    }

    public ToppingAccuracyTplPO selectOneByName(String tenantCode, String templateName) {
        return mapper.selectOne(tenantCode, null, templateName);
    }

    public List<ToppingAccuracyTplPO> selectList(String tenantCode) {
        List<ToppingAccuracyTplPO> list = mapper.selectList(tenantCode);
        return list;
    }

    public PageInfo<ToppingAccuracyTplPO> search(String tenantCode, String templateCode, String templateName,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        ToppingAccuracyTplQuery query = new ToppingAccuracyTplQuery();
        query.setTenantCode(tenantCode);
        query.setTemplateCode(StringUtils.isBlank(templateCode) ? null : templateCode);
        query.setTemplateName(StringUtils.isBlank(templateName) ? null : templateName);
        List<ToppingAccuracyTplPO> list = mapper.search(query);

        PageInfo<ToppingAccuracyTplPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ToppingAccuracyTplPO po) {
        return mapper.insert(po);
    }

    public int update(ToppingAccuracyTplPO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String toppingCode) {
        return mapper.delete(tenantCode, toppingCode);
    }
}
