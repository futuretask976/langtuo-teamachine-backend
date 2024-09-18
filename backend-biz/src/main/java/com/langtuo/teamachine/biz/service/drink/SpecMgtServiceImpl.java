package com.langtuo.teamachine.biz.service.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.SpecDTO;
import com.langtuo.teamachine.api.model.drink.SpecItemDTO;
import com.langtuo.teamachine.api.request.drink.SpecPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.SpecMgtService;
import com.langtuo.teamachine.dao.accessor.drink.SpecAccessor;
import com.langtuo.teamachine.dao.accessor.drink.SpecItemAccessor;
import com.langtuo.teamachine.dao.accessor.drink.TeaUnitAccessor;
import com.langtuo.teamachine.dao.po.drink.SpecPO;
import com.langtuo.teamachine.dao.po.drink.SpecItemPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SpecMgtServiceImpl implements SpecMgtService {
    @Resource
    private SpecAccessor specAccessor;

    @Resource
    private SpecItemAccessor specItemAccessor;

    @Resource
    private TeaUnitAccessor teaUnitAccessor;

    @Override
    public TeaMachineResult<List<SpecDTO>> list(String tenantCode) {
        try {
            List<SpecPO> list = specAccessor.list(tenantCode);
            List<SpecDTO> dtoList = convert(list);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("specMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<SpecDTO>> search(String tenantName, String specCode, String specName,
            int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<SpecPO> pageInfo = specAccessor.search(tenantName, specCode, specName,
                    pageNum, pageSize);
            List<SpecDTO> dtoList = convert(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("specMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<SpecDTO> getByCode(String tenantCode, String specCode) {
        try {
            SpecPO po = specAccessor.getBySpecCode(tenantCode, specCode);
            SpecDTO dto = convert(po);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("specMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<SpecDTO> getByName(String tenantCode, String specName) {
        try {
            SpecPO po = specAccessor.getBySpecName(tenantCode, specName);
            SpecDTO dto = convert(po);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("specMgtService|getByName|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(SpecPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        SpecPO po = convert(request);
        List<SpecItemPO> specItemPOList = convertToSpecItemPO(request);
        if (request.isPutNew()) {
            return putNew(po, specItemPOList);
        } else {
            return putUpdate(po, specItemPOList);
        }
    }

    private TeaMachineResult<Void> putNew(SpecPO po, List<SpecItemPO> specItemPOList) {
        try {
            SpecPO exist = specAccessor.getBySpecCode(po.getTenantCode(), po.getSpecCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = specAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("specMgtService|putNewSpec|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
            }

            int deleted = specItemAccessor.deleteBySpecCode(po.getTenantCode(), po.getSpecCode());
            for (SpecItemPO specItemPO : specItemPOList) {
                int inserted4Item = specItemAccessor.insert(specItemPO);
                if (inserted4Item != CommonConsts.NUM_ONE) {
                    log.error("specMgtService|putNewSpecItem|error|" + inserted4Item);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("specMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(SpecPO po, List<SpecItemPO> specItemPOList) {
        try {
            SpecPO exist = specAccessor.getBySpecCode(po.getTenantCode(), po.getSpecCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = specAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("specMgtService|putUpdateSpec|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }

            int deleted = specItemAccessor.deleteBySpecCode(po.getTenantCode(), po.getSpecCode());
            for (SpecItemPO specItemPO : specItemPOList) {
                int inserted4Item = specItemAccessor.insert(specItemPO);
                if (inserted4Item != CommonConsts.NUM_ONE) {
                    log.error("specMgtService|putUpdateSpecItem|error|" + inserted4Item);
                    return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
                }
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("specMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String specCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int countBySpecCode = teaUnitAccessor.countBySpecCode(tenantCode, specCode);
            if (countBySpecCode == CommonConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted4Spec = specAccessor.deleteBySpecCode(tenantCode, specCode);
                int deleted4SpecSub = specItemAccessor.deleteBySpecCode(tenantCode, specCode);
                return TeaMachineResult.success();
            } else {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("specMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private List<SpecDTO> convert(List<SpecPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<SpecDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private SpecDTO convert(SpecPO po) {
        if (po == null) {
            return null;
        }

        SpecDTO dto = new SpecDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecName(po.getSpecName());
        dto.setComment(po.getComment());
        dto.setExtraInfo(po.getExtraInfo());

        List<SpecItemPO> poList = specItemAccessor.listBySpecCode(po.getTenantCode(), po.getSpecCode());
        if (!CollectionUtils.isEmpty(poList)) {
            dto.setSpecItemList(poList.stream().map(item -> convert(item)).collect(Collectors.toList()));
        }
        return dto;
    }

    private List<SpecItemDTO> convertToSpecItemDTO(List<SpecItemPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
    }

    private SpecItemDTO convert(SpecItemPO po) {
        if (po == null) {
            return null;
        }

        SpecItemDTO dto = new SpecItemDTO();
        dto.setSpecCode(po.getSpecCode());
        dto.setSpecItemCode(po.getSpecItemCode());
        dto.setSpecItemName(po.getSpecItemName());
        dto.setOuterSpecItemCode(po.getOuterSpecItemCode());
        return dto;
    }

    private SpecPO convert(SpecPutRequest request) {
        if (request == null) {
            return null;
        }

        SpecPO po = new SpecPO();
        po.setSpecCode(request.getSpecCode());
        po.setSpecName(request.getSpecName());
        po.setComment(request.getComment());
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }

    private List<SpecItemPO> convertToSpecItemPO(SpecPutRequest specPutRequest) {
        if (specPutRequest == null || CollectionUtils.isEmpty(specPutRequest.getSpecItemList())) {
            return null;
        }

        List<SpecItemPO> specItemPOList = specPutRequest.getSpecItemList().stream()
                .map(item -> {
                    SpecItemPO po = new SpecItemPO();
                    po.setSpecCode(specPutRequest.getSpecCode());
                    po.setTenantCode(specPutRequest.getTenantCode());
                    po.setSpecCode(specPutRequest.getSpecCode());
                    po.setSpecItemCode(item.getSpecItemCode());
                    po.setSpecItemName(item.getSpecItemName());
                    po.setOuterSpecItemCode(item.getOuterSpecItemCode());
                    return po;
                }).collect(Collectors.toList());
        return specItemPOList;
    }
}
