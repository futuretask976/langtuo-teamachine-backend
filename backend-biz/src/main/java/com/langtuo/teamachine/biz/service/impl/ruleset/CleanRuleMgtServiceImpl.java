package com.langtuo.teamachine.biz.service.impl.ruleset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleDispatchDTO;
import com.langtuo.teamachine.api.model.ruleset.CleanRuleStepDTO;
import com.langtuo.teamachine.api.request.ruleset.CleanRuleDispatchPutRequest;
import com.langtuo.teamachine.api.request.ruleset.CleanRulePutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.ruleset.CleanRuleMgtService;
import com.langtuo.teamachine.dao.accessor.ruleset.CleanRuleAccessor;
import com.langtuo.teamachine.dao.accessor.ruleset.CleanRuleDispatchAccessor;
import com.langtuo.teamachine.dao.accessor.ruleset.CleanRuleStepAccessor;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleDispatchPO;
import com.langtuo.teamachine.dao.po.ruleset.CleanRulePO;
import com.langtuo.teamachine.dao.po.ruleset.CleanRuleStepPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CleanRuleMgtServiceImpl implements CleanRuleMgtService {
    @Resource
    private CleanRuleAccessor cleanRuleAccessor;

    @Resource
    private CleanRuleStepAccessor cleanRuleStepAccessor;

    @Resource
    private CleanRuleDispatchAccessor cleanRuleDispatchAccessor;

    @Override
    public LangTuoResult<CleanRuleDTO> getByCode(String tenantCode, String cleanRuleCode) {
        LangTuoResult<CleanRuleDTO> langTuoResult = null;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByCode(tenantCode, cleanRuleCode);
            CleanRuleDTO dto = convert(po);
            if (dto != null) {
                List<CleanRuleStepPO> poList = cleanRuleStepAccessor.selectList(tenantCode, po.getCleanRuleCode());
                dto.setCleanRuleStepList(convert(poList));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<CleanRuleDTO> getByName(String tenantCode, String cleanRuleName) {
        LangTuoResult<CleanRuleDTO> langTuoResult = null;
        try {
            CleanRulePO po = cleanRuleAccessor.selectOneByName(tenantCode, cleanRuleName);
            CleanRuleDTO dto = convert(po);
            if (dto != null) {
                List<CleanRuleStepPO> poList = cleanRuleStepAccessor.selectList(tenantCode, po.getCleanRuleCode());
                dto.setCleanRuleStepList(convert(poList));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<CleanRuleDTO>> list(String tenantCode) {
        LangTuoResult<List<CleanRuleDTO>> langTuoResult = null;
        try {
            List<CleanRulePO> cleanRulePOList = cleanRuleAccessor.selectList(tenantCode);
            List<CleanRuleDTO> teaDTOList = cleanRulePOList.stream()
                    .map(po -> {
                        CleanRuleDTO dto = convert(po);
                        List<CleanRuleStepPO> cleanRuleStepPOList = cleanRuleStepAccessor.selectList(
                                tenantCode, dto.getCleanRuleCode());
                        if (!CollectionUtils.isEmpty(cleanRuleStepPOList)) {
                            dto.setCleanRuleStepList(convert(cleanRuleStepPOList));
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(teaDTOList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<CleanRuleDTO>> search(String tenantCode, String cleanRuleCode, String cleanRuleName,
            int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<CleanRuleDTO>> langTuoResult = null;
        try {
            PageInfo<CleanRulePO> pageInfo = cleanRuleAccessor.search(tenantCode, cleanRuleCode, cleanRuleName,
                    pageNum, pageSize);
            List<CleanRuleDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> {
                        CleanRuleDTO dto = convert(po);
                        List<CleanRuleStepPO> cleanRuleStepPOList = cleanRuleStepAccessor.selectList(
                                tenantCode, cleanRuleCode);
                        if (!CollectionUtils.isEmpty(cleanRuleStepPOList)) {
                            dto.setCleanRuleStepList(convert(cleanRuleStepPOList));
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(CleanRulePutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        CleanRulePO cleanRulePO = convertToCleanRulePO(request);
        List<CleanRuleStepPO> cleanRuleStepList = convertToCleanRuleStepPO(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            CleanRulePO exist = cleanRuleAccessor.selectOneByCode(cleanRulePO.getTenantCode(),
                    cleanRulePO.getCleanRuleCode());
            if (exist != null) {
                int updated = cleanRuleAccessor.update(cleanRulePO);
            } else {
                int inserted = cleanRuleAccessor.insert(cleanRulePO);
            }

            int deleted4TeaUnit = cleanRuleStepAccessor.delete(request.getTenantCode(), request.getCleanRuleCode());
            cleanRuleStepList.forEach(item -> {
                int inserted4TeaUnit = cleanRuleStepAccessor.insert(item);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String cleanRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = cleanRuleAccessor.delete(tenantCode, cleanRuleCode);
            int deleted4CleanRuleStep = cleanRuleStepAccessor.delete(tenantCode, cleanRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> putDispatch(CleanRuleDispatchPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        List<CleanRuleDispatchPO> poList = convert(request);
        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = cleanRuleDispatchAccessor.delete(request.getTenantCode(),
                    request.getCleanRuleCode());
            poList.forEach(po -> {
                cleanRuleDispatchAccessor.insert(po);
            });

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<CleanRuleDispatchDTO> getDispatchByCleanRuleCode(String tenantCode, String cleanRuleCode) {
        LangTuoResult<CleanRuleDispatchDTO> langTuoResult = null;
        try {
            CleanRuleDispatchDTO dto = new CleanRuleDispatchDTO();
            dto.setCleanRuleCode(cleanRuleCode);

            List<CleanRuleDispatchPO> poList = cleanRuleDispatchAccessor.selectList(tenantCode, cleanRuleCode);
            if (!CollectionUtils.isEmpty(poList)) {
                dto.setShopGroupCodeList(poList.stream()
                        .map(po -> po.getShopGroupCode())
                        .collect(Collectors.toList()));
            }

            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    private CleanRuleDTO convert(CleanRulePO po) {
        if (po == null) {
            return null;
        }

        CleanRuleDTO dto = new CleanRuleDTO();
        dto.setId(po.getId());
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTenantCode(po.getTenantCode());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());
        dto.setCleanRuleCode(po.getCleanRuleCode());
        dto.setCleanRuleName(po.getCleanRuleName());
        dto.setPermitBatch(po.getPermitBatch());
        dto.setPermitRemind(po.getPermitRemind());
        return dto;
    }

    private List<CleanRuleStepDTO> convert(List<CleanRuleStepPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CleanRuleStepDTO> list = poList.stream()
                .map(po -> {
                    CleanRuleStepDTO dto = new CleanRuleStepDTO();
                    dto.setId(po.getId());
                    dto.setGmtCreated(po.getGmtCreated());
                    dto.setGmtModified(po.getGmtModified());
                    dto.setTenantCode(po.getTenantCode());
                    dto.setCleanRuleCode(po.getCleanRuleCode());
                    dto.setCleanContent(po.getCleanContent());
                    dto.setRemindContent(po.getRemindContent());
                    dto.setRemindTitle(po.getRemindTitle());
                    dto.setSoakTime(po.getSoakTime());
                    dto.setStepIndex(po.getStepIndex());
                    dto.setSoakWashInterval(po.getSoakWashInterval());
                    dto.setWashTime(po.getWashTime());
                    dto.setStepIndex(po.getStepIndex());
                    return dto;
                })
                .collect(Collectors.toList());
        return list;
    }

    private CleanRulePO convertToCleanRulePO(CleanRulePutRequest request) {
        if (request == null) {
            return null;
        }

        CleanRulePO po = new CleanRulePO();
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        po.setCleanRuleCode(request.getCleanRuleCode());
        po.setCleanRuleName(request.getCleanRuleName());
        po.setPermitBatch(request.getPermitBatch());
        po.setPermitRemind(request.getPermitRemind());
        return po;
    }

    private List<CleanRuleStepPO> convertToCleanRuleStepPO(CleanRulePutRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getCleanRuleStepList())) {
            return null;
        }

        List<CleanRuleStepPO> list = request.getCleanRuleStepList().stream()
                .map(cleanRuleStepPutRequest -> {
                    CleanRuleStepPO po = new CleanRuleStepPO();
                    po.setTenantCode(request.getTenantCode());
                    po.setCleanRuleCode(cleanRuleStepPutRequest.getCleanRuleCode());
                    po.setCleanContent(cleanRuleStepPutRequest.getCleanContent());
                    po.setRemindContent(cleanRuleStepPutRequest.getRemindContent());
                    po.setRemindTitle(cleanRuleStepPutRequest.getRemindTitle());
                    po.setSoakTime(cleanRuleStepPutRequest.getSoakTime());
                    po.setStepIndex(cleanRuleStepPutRequest.getStepIndex());
                    po.setSoakWashInterval(cleanRuleStepPutRequest.getSoakWashInterval());
                    po.setWashTime(cleanRuleStepPutRequest.getWashTime());
                    po.setStepIndex(cleanRuleStepPutRequest.getStepIndex());
                    return po;
                }).collect(Collectors.toList());
        return list;
    }

    private List<CleanRuleDispatchPO> convert(CleanRuleDispatchPutRequest request) {
        String tenantCode = request.getTenantCode();
        String cleanRuleCode = request.getCleanRuleCode();

        return request.getShopGroupCodeList().stream()
                .map(shopGroupCode -> {
                    CleanRuleDispatchPO po = new CleanRuleDispatchPO();
                    po.setTenantCode(tenantCode);
                    po.setCleanRuleCode(cleanRuleCode);
                    po.setShopGroupCode(shopGroupCode);
                    return po;
                }).collect(Collectors.toList());
    }
}
