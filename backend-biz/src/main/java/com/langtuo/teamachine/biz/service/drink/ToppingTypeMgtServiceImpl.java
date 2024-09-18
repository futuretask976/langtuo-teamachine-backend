package com.langtuo.teamachine.biz.service.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingTypeDTO;
import com.langtuo.teamachine.api.request.drink.ToppingTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.ToppingTypeMgtService;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.drink.ToppingTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingTypePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ToppingTypeMgtServiceImpl implements ToppingTypeMgtService {
    @Resource
    private ToppingTypeAccessor toppingTypeAccessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Override
    public TeaMachineResult<List<ToppingTypeDTO>> list(String tenantCode) {
        try {
            List<ToppingTypePO> list = toppingTypeAccessor.selectList(tenantCode);
            List<ToppingTypeDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("toppingTypeMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<ToppingTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;
        
        try {
            PageInfo<ToppingTypePO> pageInfo = toppingTypeAccessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingTypeDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("toppingTypeMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<ToppingTypeDTO> getByCode(String tenantCode, String toppingTypeCode) {
        try {
            ToppingTypePO toppingTypePO = toppingTypeAccessor.getByToppingTypeCode(tenantCode, toppingTypeCode);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);
            return TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("toppingTypeMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<ToppingTypeDTO> getByName(String tenantCode, String toppingTypeName) {
        try {
            ToppingTypePO toppingTypePO = toppingTypeAccessor.getByToppingTypeName(tenantCode, toppingTypeName);
            ToppingTypeDTO tenantDTO = convert(toppingTypePO);

            return TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("toppingTypeMgtService|getByName|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(ToppingTypePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ToppingTypePO toppingTypePO = convert(request);
        if (request.isPutNew()) {
            return putNew(toppingTypePO);
        } else {
            return putUpdate(toppingTypePO);
        }
    }

    private TeaMachineResult<Void> putNew(ToppingTypePO po) {
        try {
            ToppingTypePO exist = toppingTypeAccessor.getByToppingTypeCode(po.getTenantCode(), po.getToppingTypeCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = toppingTypeAccessor.insert(po);
            if (inserted != CommonConsts.NUM_ONE) {
                log.error("toppingTypeMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("toppingTypeMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ToppingTypePO po) {
        try {
            ToppingTypePO exist = toppingTypeAccessor.getByToppingTypeCode(po.getTenantCode(), po.getToppingTypeCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = toppingTypeAccessor.update(po);
            if (updated != CommonConsts.NUM_ONE) {
                log.error("toppingTypeMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("toppingTypeMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String toppingTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int countByToppingTypeCode = toppingAccessor.countByToppingTypeCode(tenantCode, toppingTypeCode);
            if (countByToppingTypeCode == CommonConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted = toppingTypeAccessor.delete(tenantCode, toppingTypeCode);
                return TeaMachineResult.success();
            } else {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("toppingTypeMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private ToppingTypeDTO convert(ToppingTypePO po) {
        if (po == null) {
            return null;
        }

        ToppingTypeDTO dto = new ToppingTypeDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setToppingTypeCode(po.getToppingTypeCode());
        dto.setToppingTypeName(po.getToppingTypeName());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());

        int toppingCount = toppingAccessor.countByToppingTypeCode(po.getTenantCode(), po.getToppingTypeCode());
        dto.setToppingCount(toppingCount);

        return dto;
    }

    private ToppingTypePO convert(ToppingTypePutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingTypePO po = new ToppingTypePO();
        po.setToppingTypeCode(request.getToppingTypeCode());
        po.setToppingTypeName(request.getToppingTypeName());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
