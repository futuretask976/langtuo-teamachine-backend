package com.langtuo.teamachine.dao.accessor;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.MachineDeployMapper;
import com.langtuo.teamachine.dao.mapper.MachineModelMapper;
import com.langtuo.teamachine.dao.po.MachineDeployPO;
import com.langtuo.teamachine.dao.po.MachineModelPO;
import com.langtuo.teamachine.dao.query.MachineDeployQuery;
import com.langtuo.teamachine.dao.query.MachineModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MachineDeployAccessor {
    @Resource
    private MachineDeployMapper mapper;

    public MachineDeployPO selectOne(String tenantCode, String deployCode) {
        return mapper.selectOne(tenantCode, deployCode);
    }

    public List<MachineDeployPO> selectList(String tenantCode) {
        List<MachineDeployPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<MachineDeployPO> search(String tenantCode, String deployCode, String shopCode, Integer state,
            int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineDeployQuery machineDeployQuery = new MachineDeployQuery();
        machineDeployQuery.setTenantCode(tenantCode);
        machineDeployQuery.setDeplyCode(StringUtils.isBlank(deployCode) ? null : deployCode);
        machineDeployQuery.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        machineDeployQuery.setState(state);
        List<MachineDeployPO> list = mapper.search(machineDeployQuery);

        PageInfo<MachineDeployPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(MachineDeployPO machineDeployPO) {
        return mapper.insert(machineDeployPO);
    }

    public int update(MachineDeployPO machineDeployPO) {
        return mapper.update(machineDeployPO);
    }

    public int delete(String tenantCode, String deployCode) {
        return mapper.delete(tenantCode, deployCode);
    }
}
