package com.langtuo.teamachine.biz.service.impl.rule;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDispatchDTO;
import com.langtuo.teamachine.api.model.rule.WarningRuleDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.request.rule.WarningRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.rule.WarningRulePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.rule.WarningRuleMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleAccessor;
import com.langtuo.teamachine.dao.accessor.rule.WarningRuleDispatchAccessor;
import com.langtuo.teamachine.dao.po.rule.WarningRuleDispatchPO;
import com.langtuo.teamachine.dao.po.rule.WarningRulePO;
import com.langtuo.teamachine.mqtt.publish.MqttPublisher4Console;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getModel;

@Component
@Slf4j
public class WarningRuleMgtServiceImpl implements WarningRuleMgtService {
    @Resource
    private WarningRuleAccessor warningRuleAccessor;

    @Resource
    private WarningRuleDispatchAccessor warningRuleDispatchAccessor;

    @Resource
    private ShopMgtService shopMgtService;

    @Resource
    private MqttPublisher4Console mqttPublisher4Console;

    @Override
    public TeaMachineResult<WarningRuleDTO> getByCode(String tenantCode, String warningRuleCode) {
        TeaMachineResult<WarningRuleDTO> teaMachineResult;
        try {
            WarningRulePO po = warningRuleAccessor.selectOneByCode(tenantCode, warningRuleCode);
            WarningRuleDTO dto = convertToWarningRuleDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<WarningRuleDTO> getByName(String tenantCode, String warningRuleName) {
        TeaMachineResult<WarningRuleDTO> teaMachineResult;
        try {
            WarningRulePO po = warningRuleAccessor.selectOneByName(tenantCode, warningRuleName);
            WarningRuleDTO dto = convertToWarningRuleDTO(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<WarningRuleDTO>> list(String tenantCode) {
        TeaMachineResult<List<WarningRuleDTO>> teaMachineResult;
        try {
            List<WarningRulePO> poList = warningRuleAccessor.selectList(tenantCode);
            List<WarningRuleDTO> dtoList = convertToWarningRuleDTO(poList);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<List<WarningRuleDTO>> listByShopCode(String tenantCode, String shopCode) {
        TeaMachineResult<List<WarningRuleDTO>> teaMachineResult;
        try {
            ShopDTO shopDTO = getModel(shopMgtService.getByCode(tenantCode, shopCode));
            if (shopDTO == null) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<WarningRuleDispatchPO> warningRuleDispatchPOList = warningRuleDispatchAccessor.selectListByShopGroupCode(
                    tenantCode, shopDTO.getShopGroupCode());
            if (CollectionUtils.isEmpty(warningRuleDispatchPOList)) {
                teaMachineResult = TeaMachineResult.success();
            }

            List<String> warningRuleCodeList = warningRuleDispatchPOList.stream()
                    .map(WarningRuleDispatchPO::getWarningRuleCode)
                    .collect(Collectors.toList());
            List<WarningRulePO> warningRulePOList = warningRuleAccessor.selectListByWarningRuleCode(tenantCode,
                    warningRuleCodeList);
            List<WarningRuleDTO> drainRuleDTOList = convertToWarningRuleDTO(warningRulePOList);
            teaMachineResult = TeaMachineResult.success(drainRuleDTOList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<WarningRuleDTO>> search(String tenantCode, String warningRuleCode,
            String warningRuleName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<WarningRuleDTO>> teaMachineResult;
        try {
            PageInfo<WarningRulePO> pageInfo = warningRuleAccessor.search(tenantCode, warningRuleCode,
                    warningRuleName, pageNum, pageSize);
            List<WarningRuleDTO> dtoList = convertToWarningRuleDTO(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(WarningRulePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        WarningRulePO warningRulePO = convertToWarningRuleDTO(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            WarningRulePO exist = warningRuleAccessor.selectOneByCode(warningRulePO.getTenantCode(),
                    warningRulePO.getWarningRuleCode());
            if (exist != null) {
                int updated = warningRuleAccessor.update(warningRulePO);
            } else {
                int inserted = warningRuleAccessor.insert(warningRulePO);
            }

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = warningRuleAccessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> putDispatch(WarningRuleDispatchPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<WarningRuleDispatchPO> poList = convertToWarningRuleDTO(request);
        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = warningRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getWarningRuleCode());
            poList.forEach(po -> {
                warningRuleDispatchAccessor.insert(po);
            });

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("putDispatch error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }

        // 异步发送消息准备配置信息分发
        mqttPublisher4Console.send4WarningRule(
                request.getTenantCode(), request.getWarningRuleCode());

        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<WarningRuleDispatchDTO> getDispatchByCode(String tenantCode, String warningRuleCode) {
        TeaMachineResult<WarningRuleDispatchDTO> teaMachineResult;
        try {
            WarningRuleDispatchDTO dto = new WarningRuleDispatchDTO();
            dto.setWarningRuleCode(warningRuleCode);

            List<WarningRuleDispatchPO> poList = warningRuleDispatchAccessor.selectList(tenantCode, warningRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getDispatchByWarningRuleCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    private List<WarningRuleDTO> convertToWarningRuleDTO(List<WarningRulePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<WarningRuleDTO> list = poList.stream()
                .map(po -> convertToWarningRuleDTO(po))
                .collect(Collectors.toList());
        return list;
    }

    private WarningRuleDTO convertToWarningRuleDTO(WarningRulePO po) {
        if (po == null) {
            return null;
        }

        WarningRuleDTO dto = new WarningRuleDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setWarningRuleCode(po.getWarningRuleCode());
        dto.setWarningRuleName(po.getWarningRuleName());
        dto.setWarningContent(po.getWarningContent());
        dto.setWarningType(po.getWarningType());
        dto.setThreshold(po.getThreshold());
        dto.setThresholdMode(po.getThresholdMode());
        dto.setComment(po.getComment());
        return dto;
    }

    private WarningRulePO convertToWarningRuleDTO(WarningRulePutRequest request) {
        if (request == null) {
            return null;
        }

        WarningRulePO po = new WarningRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        po.setWarningRuleCode(request.getWarningRuleCode());
        po.setWarningRuleName(request.getWarningRuleName());
        po.setWarningContent(request.getWarningContent());
        po.setWarningType(request.getWarningType());
        po.setThreshold(request.getThreshold());
        po.setThresholdMode(request.getThresholdMode());
        po.setComment(request.getComment());
        return po;
    }

    private List<WarningRuleDispatchPO> convertToWarningRuleDTO(WarningRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String warningRuleCode = request.getWarningRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    WarningRuleDispatchPO po = new WarningRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setWarningRuleCode(warningRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
