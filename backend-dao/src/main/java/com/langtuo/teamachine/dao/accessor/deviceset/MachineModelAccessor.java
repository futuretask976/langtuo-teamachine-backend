package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.deviceset.MachineModelMapper;
import com.langtuo.teamachine.dao.po.deviceset.MachineModelPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineModelAccessor {
    @Resource
    private MachineModelMapper mapper;

    public MachineModelPO selectOne(String modelCode) {
        return mapper.selectOne(modelCode);
    }

    public List<MachineModelPO> selectList() {
        List<MachineModelPO> list = mapper.selectList();

        return list;
    }

    public PageInfo<MachineModelPO> search(String modelCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineModelQuery machineModelQuery = new MachineModelQuery();
        machineModelQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        List<MachineModelPO> list = mapper.search(machineModelQuery);

        PageInfo<MachineModelPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MachineModelPO po) {
        return mapper.insert(po);
    }

    public int update(MachineModelPO po) {
        return mapper.update(po);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
