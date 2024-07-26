package com.langtuo.teamachine.dao.accessor.deviceset;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.dao.mapper.deviceset.DeployMapper;
import com.langtuo.teamachine.dao.po.deviceset.DeployPO;
import com.langtuo.teamachine.dao.query.deviceset.MachineDeployQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class DeployAccessor {
    @Resource
    private DeployMapper mapper;

    public DeployPO selectOne(String tenantCode, String deployCode) {
        return mapper.selectOne(tenantCode, deployCode);
    }

    public List<DeployPO> selectList(String tenantCode) {
        List<DeployPO> list = mapper.selectList(tenantCode);

        return list;
    }

    public PageInfo<DeployPO> search(String tenantCode, String deployCode, String machineCode, String shopCode,
            Integer state, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        MachineDeployQuery machineDeployQuery = new MachineDeployQuery();
        machineDeployQuery.setTenantCode(tenantCode);
        machineDeployQuery.setDeployCode(StringUtils.isBlank(deployCode) ? null : deployCode);
        machineDeployQuery.setShopCode(StringUtils.isBlank(shopCode) ? null : shopCode);
        machineDeployQuery.setState(state);
        List<DeployPO> list = mapper.search(machineDeployQuery);

        PageInfo<DeployPO> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    public int insert(DeployPO po) {
        return mapper.insert(po);
    }

    public int update(DeployPO po) {
        return mapper.update(po);
    }

    public int delete(String tenantCode, String deployCode) {
        return mapper.delete(tenantCode, deployCode);
    }
}
