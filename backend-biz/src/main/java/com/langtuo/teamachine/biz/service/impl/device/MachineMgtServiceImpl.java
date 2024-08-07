package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.device.MachineDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.device.MachineActivatePutRequest;
import com.langtuo.teamachine.api.request.device.MachineUpdatePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.device.MachineMgtService;
import com.langtuo.teamachine.dao.accessor.device.MachineAccessor;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import com.langtuo.teamachine.dao.po.device.MachinePO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
            ShopPO shopPO = shopAccessor.selectOneByName(tenantCode, shopName);
            if (shopPO == null && StringUtils.isNotBlank(shopName)) {
                return LangTuoResult.success(new PageDTO<MachineDTO>(null, 0, pageNum, pageSize));
            }
            String shopCode = shopPO == null ? null : shopPO.getShopCode();

            PageInfo<MachinePO> pageInfo = machineAccessor.search(tenantCode, screenCode, elecBoardCode, modelCode,
                    shopCode, pageNum, pageSize);
            List<MachineDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<MachineDTO>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
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
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<MachineDTO>> listByShopCode(String tenantCode, String shopCode) {
        LangTuoResult<List<MachineDTO>> langTuoResult = null;
        try {
            List<MachinePO> list = machineAccessor.selectListByShopCode(tenantCode, shopCode);
            List<MachineDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> activate(MachineActivatePutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            DeployPO deployPO = convert(request);
            DeployPO existDeployPO = deployAccessor.selectOne(request.getTenantCode(),
                    request.getDeployCode());
            if (existDeployPO != null) {
                int updated = deployAccessor.update(deployPO);
            } else {
                int inserted = deployAccessor.insert(deployPO);
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
            log.error("activate error: " + e.getMessage(), e);
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
            log.error("update error: " + e.getMessage(), e);
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
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
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
        dto.setId(po.getId());
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

        ShopPO shopPO = shopAccessor.selectOneByCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopCode(shopPO.getShopCode());
            dto.setShopName(shopPO.getShopName());
        }
        return dto;
    }

    private DeployPO convert(MachineActivatePutRequest request) {
        if (request == null) {
            return null;
        }

        DeployPO po = new DeployPO();
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
