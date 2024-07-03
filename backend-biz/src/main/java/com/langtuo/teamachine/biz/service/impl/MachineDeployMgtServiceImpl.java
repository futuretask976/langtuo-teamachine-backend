package com.langtuo.teamachine.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.MachineDeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.MachineDeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.MachineDeployMgtService;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import com.langtuo.teamachine.dao.accessor.MachineDeployAccessor;
import com.langtuo.teamachine.dao.accessor.ShopAccessor;
import com.langtuo.teamachine.dao.po.MachineDeployPO;
import com.langtuo.teamachine.dao.po.ShopPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineDeployMgtServiceImpl implements MachineDeployMgtService {
    @Resource
    private MachineDeployAccessor machineDeployAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public LangTuoResult<PageDTO<MachineDeployDTO>> list(String tenantCode) {
        LangTuoResult<PageDTO<MachineDeployDTO>> langTuoResult = null;
        try {
            List<MachineDeployPO> list = machineDeployAccessor.selectList(tenantCode);
            List<MachineDeployDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<MachineDeployDTO>> search(String tenantCode, String deployCode, String shopName,
            Integer state, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<MachineDeployDTO>> langTuoResult = null;
        try {
            PageInfo<MachineDeployPO> pageInfo = machineDeployAccessor.search(tenantCode, deployCode, shopName, state,
                    pageNum, pageSize);
            List<MachineDeployDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            PageDTO<MachineDeployDTO> pageDTO = new PageDTO<>();
            pageDTO.setList(dtoList);
            pageDTO.setPageNum(pageNum);
            pageDTO.setPageSize(pageSize);
            pageDTO.setTotal(pageInfo.getTotal());

            langTuoResult = LangTuoResult.success(pageDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<MachineDeployDTO> get(String tenantCode, String deployCode) {
        LangTuoResult<MachineDeployDTO> langTuoResult = null;
        try {
            MachineDeployDTO machineModelDTO = convert(machineDeployAccessor.selectOne(tenantCode, deployCode));

            langTuoResult = LangTuoResult.success(machineModelDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(MachineDeployPutRequest machineDeployPutRequest) {
        if (machineDeployPutRequest == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        MachineDeployPO machineDeployPO = convert(machineDeployPutRequest);

        LangTuoResult<Void> langTuoResult = null;
        try {
            MachineDeployPO exist = machineDeployAccessor.selectOne(machineDeployPO.getTenantCode(),
                    machineDeployPO.getDeployCode());
            if (exist != null) {
                int updated = machineDeployAccessor.update(machineDeployPO);
            } else {
                int inserted = machineDeployAccessor.insert(machineDeployPO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String deployCode) {
        if (StringUtils.isEmpty(tenantCode) || StringUtils.isBlank(deployCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = machineDeployAccessor.delete(tenantCode, deployCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    public LangTuoResult<String> genRandomStr() {
        String randomStr = DeployUtils.genRandomStr(20);
        return LangTuoResult.success(randomStr);
    }

    private MachineDeployPO convert(MachineDeployPutRequest request) {
        if (request == null) {
            return null;
        }

        MachineDeployPO po = new MachineDeployPO();
        po.setDeployCode(request.getDeployCode());
        po.setModelCode(request.getModelCode());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setState(request.getState());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }

    private MachineDeployDTO convert(MachineDeployPO po) {
        if (po == null) {
            return null;
        }

        MachineDeployDTO dto = new MachineDeployDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setDeployCode(po.getDeployCode());
        dto.setModelCode(po.getModelCode());
        dto.setMachineCode(po.getMachineCode());
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
}
