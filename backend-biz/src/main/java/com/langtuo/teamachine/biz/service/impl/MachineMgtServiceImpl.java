package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineMgtService;
import com.langtuo.teamachine.dao.accessor.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.MachineDeployAccessor;
import com.langtuo.teamachine.dao.accessor.ShopAccessor;
import com.langtuo.teamachine.dao.po.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineMgtServiceImpl implements MachineMgtService {
    @Resource
    private MachineAccessor machineAccessor;

    @Resource
    private MachineDeployAccessor machineDeployAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public LangTuoResult<MachineDTO> get(String tenantCode, String machineCode) {
        MachinePO machinePO = machineAccessor.selectOne(tenantCode, machineCode);
        MachineDTO adminRoleDTO = convert(machinePO);
        return LangTuoResult.success(adminRoleDTO);
    }

    @Override
    public LangTuoResult<PageDTO<MachineDTO>> search(String tenantCode, String screenCode, String elecBoardCode,
            String modelCode, String shopName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<MachineDTO>> langTuoResult = null;
        try {
            ShopPO shopPO = shopAccessor.selectOne(tenantCode, null, shopName);
            if (shopPO == null && StringUtils.isNotBlank(shopName)) {
                return LangTuoResult.success(new PageDTO<MachineDTO>(null, 0, pageNum, pageSize));
            }
            String shopCode = shopPO == null ? null : shopPO.getShopCode();

            PageInfo<MachinePO> pageInfo = machineAccessor.search(tenantCode, screenCode, elecBoardCode, modelCode,
                    shopCode, pageNum, pageSize);
            List<MachineDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> {
                        return convert(po);
                    })
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<MachineDTO>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<MachineDTO>> list(String tenantCode) {
        LangTuoResult<List<MachineDTO>> langTuoResult = null;
        try {
            List<MachinePO> list = machineAccessor.selectList(tenantCode);
            List<MachineDTO> dtoList = list.stream()
                    .map(po -> {
                        return convert(po);
                    })
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> activate(MachineActivatePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            MachineDeployPO machineDeployPO = convert(request);
            MachineDeployPO existMachineDeployPO = machineDeployAccessor.selectOne(request.getTenantCode(),
                    request.getDeployCode());
            if (existMachineDeployPO != null) {
                int updated = machineDeployAccessor.update(machineDeployPO);
            } else {
                int inserted = machineDeployAccessor.insert(machineDeployPO);
            }

            MachinePO machinePO = convertToMachinePO(request);
            MachinePO existMachinePO = machineAccessor.selectOne(request.getTenantCode(), request.getMachineCode());
            if (existMachinePO != null) {
                int updated = machineAccessor.update(machinePO);
            } else {
                int inserted = machineAccessor.insert(machinePO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> update(MachineUpdatePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            MachinePO po = convert(request);
            MachinePO exist = machineAccessor.selectOne(request.getTenantCode(), request.getMachineCode());
            if (exist != null) {
                int updated = machineAccessor.update(po);
            } else {
                int inserted = machineAccessor.insert(po);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String machineCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(machineCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = machineAccessor.delete(tenantCode, machineCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private MachineDTO convert(MachinePO po) {
        if (po == null) {
            return null;
        }

        MachineDTO dto = new MachineDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setMachineCode(po.getMachineCode());
        dto.setElecBoardCode(po.getElecBoardCode());
        dto.setScreenCode(po.getScreenCode());
        dto.setModelCode(po.getModelCode());
        dto.setMachineName(po.getMachineName());
        dto.setMaintainUntil(po.getMaintainUntil());
        dto.setValidUntil(po.getValidUntil());
        dto.setState(po.getState());
        dto.setTenantCode(po.getTenantCode());
        dto.setExtraInfo(po.getExtraInfo());

        ShopPO shopPO = shopAccessor.selectOne(po.getTenantCode(), po.getShopCode(), null);
        if (shopPO != null) {
            dto.setShopCode(shopPO.getShopCode());
            dto.setShopName(shopPO.getShopName());
        }
        return dto;
    }

    private MachineDeployPO convert(MachineActivatePutRequest request) {
        if (request == null) {
            return null;
        }

        MachineDeployPO po = new MachineDeployPO();
        po.setDeployCode(request.getDeployCode());
        po.setModelCode(request.getModelCode());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setState(1);
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private MachinePO convertToMachinePO(MachineActivatePutRequest request) {
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
        po.setState(1);
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
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
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
