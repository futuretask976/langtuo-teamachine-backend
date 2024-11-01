package com.langtuo.teamachine.biz.service.device;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.biz.aync.AsyncDispatcher;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.device.ModelAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.LocaleUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.biz.convert.device.MachineMgtConvertor.convert;
import static com.langtuo.teamachine.biz.convert.device.MachineMgtConvertor.convertToMachinePO;

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
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ModelAccessor modelAccessor;

    @Resource
    private AsyncDispatcher asyncDispatcher;

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<MachineDTO> getByMachineCode(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            MachinePO machinePO = machineAccessor.getByMachineCode(tenantCode, machineCode);
            MachineDTO adminRoleDTO = convert(machinePO);
            return TeaMachineResult.success(adminRoleDTO);
        } catch (Exception e) {
            log.error("|machineMgtService|getByMachineCode|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<PageDTO<MachineDTO>> search(String tenantCode, String machineCode, String screenCode,
            String elecBoardCode, String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<MachinePO> pageInfo = machineAccessor.search(tenantCode, machineCode, screenCode, elecBoardCode,
                    shopCode, pageNum, pageSize);
            List<MachineDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            return TeaMachineResult.success(new PageDTO<MachineDTO>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("|machineMgtService|search|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<MachineDTO>> list(String tenantCode) {
        if (StringUtils.isBlank(tenantCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            List<MachinePO> list = machineAccessor.list(tenantCode);
            List<MachineDTO> dtoList = convert(list);

            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("|machineMgtService|list|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TeaMachineResult<List<MachineDTO>> listByShopCode(String tenantCode, String shopCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(shopCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            List<MachinePO> list = machineAccessor.listByShopCode(tenantCode, shopCode);
            List<MachineDTO> dtoList = convert(list);

            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("|machineMgtService|listByShopCode|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<MachineDTO> activate(MachineActivatePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doActivate(request);
        } catch (Exception e) {
            log.error("|machineMgtService|activate|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(MachineUpdatePutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            MachinePO po = convert(request);
            TeaMachineResult<Void> result = doPut(po);

            // 异步发送消息准备配置信息分发
            JSONObject jsonPayload = new JSONObject();
            jsonPayload.put(CommonConsts.JSON_KEY_BIZ_CODE, CommonConsts.BIZ_CODE_MACHINE_UPDATED);
            jsonPayload.put(CommonConsts.JSON_KEY_TENANT_CODE, request.getTenantCode());
            jsonPayload.put(CommonConsts.JSON_KEY_MACHINE_CODE, request.getMachineCode());
            asyncDispatcher.dispatch(jsonPayload);

            return result;
        } catch (Exception e) {
            log.error("|machineMgtService|put|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByMachineCode(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            return doDeleteByMachineCode(tenantCode, machineCode);
        } catch (Exception e) {
            log.error("|machineMgtService|delete|fatal|" + e.getMessage() + "|", e);
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<MachineDTO> doActivate(MachineActivatePutRequest request) {
        // 激活时，设备端是不知道 tenantCode 的，只能通过 deployCode 查找和更新
        DeployPO existDeployPO = deployAccessor.getByDeployCode(request.getDeployCode());
        if (existDeployPO == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        if (!existDeployPO.getMachineCode().equals(request.getMachineCode())) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_DEPLOY_MACHINE_NOT_MATCH));
        }

        existDeployPO.setState(1);
        int updated = deployAccessor.update(existDeployPO);
        if (updated != 1) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }

        MachinePO existMachinePO = machineAccessor.getByMachineCode(existDeployPO.getTenantCode(),
                existDeployPO.getMachineCode());
        if (existMachinePO == null) {
            MachinePO machinePO = convertToMachinePO(request, existDeployPO);
            int inserted = machineAccessor.insert(machinePO);
            if (inserted != 1) {
                return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }
            return TeaMachineResult.success(convert(machinePO));
        } else {
            return TeaMachineResult.success(convert(existMachinePO));
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doPut(MachinePO po) {
        MachinePO exist = machineAccessor.getByMachineCode(po.getTenantCode(), po.getMachineCode());
        if (exist == null) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }

        int updated = machineAccessor.update(po);
        if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
            return TeaMachineResult.error(LocaleUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
        return TeaMachineResult.success();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private TeaMachineResult<Void> doDeleteByMachineCode(String tenantCode, String machineCode) {
        int deleted = machineAccessor.deleteByMachineCode(tenantCode, machineCode);
        return TeaMachineResult.success();
    }

    @Transactional(readOnly = true)
    private TeaMachineResult<MachineDTO> doGetByMachineCode(String tenantCode, String machineCode) {
        MachinePO machinePO = machineAccessor.getByMachineCode(tenantCode, machineCode);
        MachineDTO adminRoleDTO = convert(machinePO);
        return TeaMachineResult.success(adminRoleDTO);
    }
}
