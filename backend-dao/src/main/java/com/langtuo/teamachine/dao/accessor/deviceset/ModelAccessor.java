package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.deviceset.ModelMapper;
import com.langtuo.teamachine.dao.po.deviceset.ModelPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ModelAccessor {
    @Resource
    private ModelMapper mapper;

    public ModelPO selectOne(String modelCode) {
        return mapper.selectOne(modelCode);
    }

    public List<ModelPO> selectList() {
        List<ModelPO> list = mapper.selectList();

        return list;
    }

    public PageInfo<ModelPO> search(String modelCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineModelQuery machineModelQuery = new MachineModelQuery();
        machineModelQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        List<ModelPO> list = mapper.search(machineModelQuery);

        PageInfo<ModelPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(ModelPO po) {
        return mapper.insert(po);
    }

    public int update(ModelPO po) {
        return mapper.update(po);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
