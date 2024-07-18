package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.deviceset.MachineMapper;
import com.langtuo.teamachine.dao.po.deviceset.MachinePO;
import com.langtuo.teamachine.dao.query.deviceset.MachineQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineAccessor {
    @Resource
    private MachineMapper mapper;

    public MachinePO selectOne(String tenantCode, String machineCode) {
        return mapper.selectOne(tenantCode, machineCode, null);
    }

    public List<MachinePO> selectList(String tenantCode) {
        List<MachinePO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<MachinePO> search(String tenantCode, String screenCode, String elecBoardCode,
            String modelCode, String shopCode, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineQuery machineQuery = new MachineQuery();
        machineQuery.setTenantCode(tenantCode);
        machineQuery.setScreenCode(StringUtils.isBlank(screenCode) ? null : screenCode);
        machineQuery.setElecBoardCode(StringUtils.isBlank(elecBoardCode) ? null : elecBoardCode);
        machineQuery.setModelCode(StringUtils.isBlank(modelCode) ? null : modelCode);
        machineQuery.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        List<MachinePO> list = mapper.search(machineQuery);

        PageInfo<MachinePO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MachinePO MachinePO) {
        return mapper.insert(MachinePO);
    }

    public int update(MachinePO MachinePO) {
        return mapper.update(MachinePO);
    }

    public int delete(String tenantCode, String machineCode) {
        return mapper.delete(tenantCode, machineCode);
    }
}
