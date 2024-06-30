package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import com.langtuo.teamachine.dao.query.MachineModelQuery;
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

    public PageInfo<MachineModelPO> selectList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<MachineModelPO> list = mapper.selectList();

        PageInfo<MachineModelPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public PageInfo<MachineModelPO> search(String modelCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        System.out.printf("$$$$$ MachineModelAccessor#search modelCode=%s\n", modelCode);
        MachineModelQuery machineModelQuery = new MachineModelQuery();
        machineModelQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        List<MachineModelPO> list = mapper.search(machineModelQuery);

        PageInfo<MachineModelPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MachineModelPO machineModelPO) {
        return mapper.insert(machineModelPO);
    }

    public int update(MachineModelPO machineModelPO) {
        return mapper.update(machineModelPO);
    }

    public int delete(String modelCode) {
        return mapper.delete(modelCode);
    }
}
