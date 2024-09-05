package com.langtuo.teamachine.biz.service.impl.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.aync.AsyncDispatcher;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MachineMgtServiceImpl implements MachineMgtService {
    @Resource
    private MachineAccessor machineAccessor;

    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<MachineDTO> getByCode(String tenantCode, String machineCode) {
        MachinePO machinePO = machineAccessor.selectOneByMachineCode(tenantCode, machineCode);
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
            ShopPO shopPO = shopAccessor.selectOneByShopName(tenantCode, shopName);
            if (shopPO == null && StringUtils.isNotBlank(shopName)) {
                return TeaMachineResult.success(new PageDTO<MachineDTO>(null, 0, pageNum, pageSize));
            }
            String shopCode = shopPO == null ? null : shopPO.getShopCode();

            PageInfo<MachinePO> pageInfo = machineAccessor.search(tenantCode, screenCode, elecBoardCode, modelCode,
                    shopCode, pageNum, pageSize);
            List<MachineDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            teaMachineResult = TeaMachineResult.success(new PageDTO<MachineDTO>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
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
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
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
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<MachineDTO> activate(MachineActivatePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<MachineDTO> teaMachineResult;
        try {
            // 激活时，设备端是不知道 tenantCode 的，只能通过 deployCode 查找和更新
            DeployPO existDeployPO = deployAccessor.selectOneByDeployCode(request.getDeployCode());
            if (existDeployPO == null) {
                return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                        messageSource));
            }
            if (!existDeployPO.getMachineCode().equals(request.getMachineCode())) {
                return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_DEPLOY_MACHINE_NOT_MATCH,
                        messageSource));
            }

            existDeployPO.setState(1);
            int updated = deployAccessor.update(existDeployPO);
            if (updated != 1) {
                return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL,
                    messageSource));
            }

            MachinePO existMachinePO = machineAccessor.selectOneByMachineCode(existDeployPO.getTenantCode(),
                    existDeployPO.getMachineCode());
            if (existMachinePO == null) {
                MachinePO machinePO = convertToMachinePO(request, existDeployPO);
                int inserted = machineAccessor.insert(machinePO);
                if (inserted != 1) {
                    return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                            messageSource));
                }
                teaMachineResult = TeaMachineResult.success(convert(machinePO));
            } else {
                teaMachineResult = TeaMachineResult.success(convert(existMachinePO));
            }
        } catch (Exception e) {
            log.error("activate error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(MachineUpdatePutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            MachinePO po = convert(request);
            MachinePO exist = machineAccessor.selectOneByMachineCode(request.getTenantCode(), request.getMachineCode());
            if (exist != null) {
                int updated = machineAccessor.update(po);
            } else {
                int inserted = machineAccessor.insert(po);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("update error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }

        // 异步发送消息准备配置信息分发
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put(BizConsts.JSON_KEY_BIZ_CODE, BizConsts.BIZ_CODE_MACHINE_UPDATED);
        jsonPayload.put(BizConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
        jsonPayload.put(BizConsts.JSON_KEY_MACHINE_CODE, request.getMachineCode());
        asyncDispatcher.dispatch(jsonPayload);

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = machineAccessor.deleteByMachineCode(tenantCode, machineCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
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

        ShopPO shopPO = shopAccessor.selectOneByShopCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopCode(shopPO.getShopCode());
            dto.setShopName(shopPO.getShopName());
        }
        return dto;
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
