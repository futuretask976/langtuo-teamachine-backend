package com.langtuo.teamachine.biz.service.impl.device;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.device.DeployDTO;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.device.DeployPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.device.DeployMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.util.DeployUtils;
import com.langtuo.teamachine.dao.accessor.device.DeployAccessor;
import com.langtuo.teamachine.dao.po.device.DeployPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.*;

@Component
@Slf4j
public class DeployMgtServiceImpl implements DeployMgtService {
    @Resource
    private DeployAccessor deployAccessor;

    @Resource
    private ShopMgtService shopMgtService;

    @Override
    public LangTuoResult<List<DeployDTO>> list(String tenantCode) {
        LangTuoResult<List<DeployDTO>> langTuoResult = null;
        try {
            List<DeployPO> list = deployAccessor.selectList(tenantCode);
            List<DeployDTO> dtoList = convert(list);
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
            List<DeployDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(
                    dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
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
            log.error("get error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(DeployPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        DeployPO deployPO = convert(request);

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
            log.error("put error: " + e.getMessage(), e);
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
            log.error("delete error: " + e.getMessage(), e);
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

    private List<DeployDTO> convert(List<DeployPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DeployDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private DeployDTO convert(DeployPO po) {
        if (po == null) {
            return null;
        }

        DeployDTO dto = new DeployDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setDeployCode(po.getDeployCode());
        dto.setModelCode(po.getModelCode());
        dto.setMachineCode(po.getMachineCode());
        dto.setState(po.getState());
        dto.setExtraInfo(po.getExtraInfo());

        ShopDTO shopDTO = getModel(shopMgtService.getByCode(po.getTenantCode(), po.getShopCode()));
        if (shopDTO != null) {
            dto.setShopCode(shopDTO.getShopCode());
            dto.setShopName(shopDTO.getShopName());
        }
        return dto;
    }
}
