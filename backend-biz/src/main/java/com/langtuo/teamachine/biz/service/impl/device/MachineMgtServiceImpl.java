package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.*;

@Component
@Slf4j
public class MachineMgtServiceImpl implements MachineMgtService {
    @Resource
    private MachineAccessor machineAccessor;

    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private ShopMgtService shopMgtService;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public TeaMachineResult<MachineDTO> getByCode(String tenantCode, String machineCode) {
        MachinePO machinePO = machineAccessor.selectOne(tenantCode, machineCode);
        MachineDTO adminRoleDTO = convert(machinePO);
        return TeaMachineResult.success(adminRoleDTO);
    }

    @Override
    public TeaMachineResult<PageDTO<MachineDTO>> search(String tenantCode, String screenCode, String elecBoardCode,
            String modelCode, String shopName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<MachineDTO>> teaMachineResult;
        try {
            ShopDTO shopDTO = getModel(shopMgtService.getByCode(tenantCode, shopName));
            if (shopDTO == null && StringUtils.isNotBlank(shopName)) {
                return TeaMachineResult.success(new PageDTO<MachineDTO>(null, 0, pageNum, pageSize));
            }
            String shopCode = shopDTO == null ? null : shopDTO.getShopCode();

            PageInfo<MachinePO> pageInfo = machineAccessor.search(tenantCode, screenCode, elecBoardCode, modelCode,
                    shopCode, pageNum, pageSize);
            List<MachineDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(new PageDTO<MachineDTO>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<MachineDTO>> list(String tenantCode) {
        TeaMachineResult<List<MachineDTO>> teaMachineResult;
        try {
            List<MachinePO> list = machineAccessor.selectList(tenantCode);
            List<MachineDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<MachineDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<MachineDTO>> teaMachineResult;
        try {
            List<MachinePO> list = machineAccessor.selectListByShopCode(tenantCode, shopCode);
            List<MachineDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<MachineDTO> activate(MachineActivatePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<MachineDTO> teaMachineResult;
        try {
            DeployPO deployPO = convert(request);
            int updated = deployAccessor.update(deployPO);
            if (updated != 1) {
                return TeaMachineResult.error(ErrorEnum.DB_ERR_UPDATE_FAIL);
            }

            DeployPO exist = deployAccessor.selectOneByDeployCode(deployPO.getTenantCode(), deployPO.getDeployCode());
            if (exist == null) {
                return TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
            }

            MachinePO machinePO = convertToMachinePO(request, exist);
            int inserted = machineAccessor.insert(machinePO);
            if (inserted != 1) {
                return TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
            }
            teaMachineResult = TeaMachineResult.success(convert(machinePO));
        } catch (Exception e) {
            log.error("activate error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(MachineUpdatePutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            MachinePO po = convert(request);
            MachinePO exist = machineAccessor.selectOne(request.getTenantCode(), request.getMachineCode());
            if (exist != null) {
                int updated = machineAccessor.update(po);
            } else {
                int inserted = machineAccessor.insert(po);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("update error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4Machine(request.getTenantCode(), request.getMachineCode());

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = machineAccessor.delete(tenantCode, machineCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    private List<MachineDTO> convert(List<MachinePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<MachineDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private MachineDTO convert(MachinePO po) {
        if (po == null) {
            return null;
        }

        MachineDTO dto = new MachineDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setMachineCode(po.getMachineCode());
        dto.setElecBoardCode(po.getElecBoardCode());
        dto.setScreenCode(po.getScreenCode());
        dto.setModelCode(po.getModelCode());
        dto.setMachineName(po.getMachineName());
        dto.setMaintainUntil(po.getMaintainUntil());
        dto.setValidUntil(po.getValidUntil());
        dto.setState(po.getState());
        dto.setExtraInfo(po.getExtraInfo());

        ShopDTO shopDTO = getModel(shopMgtService.getByCode(po.getTenantCode(), po.getShopCode()));
        if (shopDTO != null) {
            dto.setShopCode(shopDTO.getShopCode());
            dto.setShopName(shopDTO.getShopName());
        }
        return dto;
    }

    private DeployPO convert(MachineActivatePutRequest request) {
        if (request == null) {
            return null;
        }

        DeployPO po = new DeployPO();
        po.setDeployCode(request.getDeployCode());
        po.setMachineCode(request.getMachineCode());
        po.setState(1);
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private MachinePO convertToMachinePO(MachineActivatePutRequest request, DeployPO deployPO) {
        if (request == null || deployPO == null) {
            return null;
        }

        MachinePO po = new MachinePO();
        po.setTenantCode(deployPO.getTenantCode());
        po.setMachineCode(deployPO.getMachineCode());
        po.setScreenCode(request.getScreenCode());
        po.setElecBoardCode(request.getElecBoardCode());
        po.setModelCode(deployPO.getModelCode());
        po.setShopCode(deployPO.getShopCode());
        po.setMachineName(null);
        po.setState(1);
        po.setMaintainUntil(null);
        po.setValidUntil(null);
        return po;
    }

    private MachinePO convert(MachineUpdatePutRequest request) {
        if (request == null) {
            return null;
        }

        MachinePO po = new MachinePO();
        po.setMachineCode(request.getMachineCode());
        po.setScreenCode(request.getScreenCode());
        po.setElecBoardCode(request.getElecBoardCode());
        po.setMachineName(request.getMachineName());
        po.setMaintainUntil(request.getMaintainUntil());
        po.setValidUntil(request.getValidUntil());
        po.setState(request.getState());
        po.setShopCode(request.getShopCode());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
