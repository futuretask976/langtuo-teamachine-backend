package com.langtuo.teamachine.biz.service.impl.deviceset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.deviceset.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.request.deviceset.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.deviceset.MachineDeployMgtService;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import com.langtuo.teamachine.dao.accessor.deviceset.DeployAccessor;
import com.langtuo.teamachine.dao.accessor.shopset.ShopAccessor;
import com.langtuo.teamachine.dao.po.deviceset.DeployPO;
import com.langtuo.teamachine.dao.po.shopset.ShopPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MachineDeployMgtServiceImpl implements MachineDeployMgtService {
    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public LangTuoResult<List<DeployDTO>> list(String tenantCode) {
        LangTuoResult<List<DeployDTO>> langTuoResult = null;
        try {
            List<DeployPO> list = deployAccessor.selectList(tenantCode);
            List<DeployDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<DeployDTO>> search(String tenantCode, String deployCode, String machineCode,
            String shopName, Integer state, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<DeployDTO>> langTuoResult = null;
        try {
            PageInfo<DeployPO> pageInfo = deployAccessor.search(tenantCode, deployCode, machineCode,
                    shopName, state, pageNum, pageSize);
            List<DeployDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<DeployDTO> get(String tenantCode, String deployCode) {
        LangTuoResult<DeployDTO> langTuoResult = null;
        try {
            DeployDTO machineModelDTO = convert(deployAccessor.selectOne(tenantCode, deployCode));

            langTuoResult = LangTuoResult.success(machineModelDTO);
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(DeployPutRequest deployPutRequest) {
        if (deployPutRequest == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        DeployPO deployPO = convert(deployPutRequest);

        LangTuoResult<Void> langTuoResult = null;
        try {
            DeployPO exist = deployAccessor.selectOne(deployPO.getTenantCode(),
                    deployPO.getDeployCode());
            if (exist != null) {
                int updated = deployAccessor.update(deployPO);
            } else {
                int inserted = deployAccessor.insert(deployPO);
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
            int deleted = deployAccessor.delete(tenantCode, deployCode);
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

    private DeployPO convert(DeployPutRequest request) {
        if (request == null) {
            return null;
        }

        DeployPO po = new DeployPO();
        po.setDeployCode(request.getDeployCode());
        po.setModelCode(request.getModelCode());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setState(request.getState());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }

    private DeployDTO convert(DeployPO po) {
        if (po == null) {
            return null;
        }

        DeployDTO dto = new DeployDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setDeployCode(po.getDeployCode());
        dto.setModelCode(po.getModelCode());
        dto.setMachineCode(po.getMachineCode());
        dto.setState(po.getState());
        dto.setExtraInfo(po.getExtraInfo());

        ShopPO shopPO = shopAccessor.selectOneByCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopCode(shopPO.getShopCode());
            dto.setShopName(shopPO.getShopName());
        }

        return dto;
    }
}
