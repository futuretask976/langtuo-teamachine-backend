package com.langtuo.teamachine.biz.service.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.TeaTypeMgtService;
import com.langtuo.teamachine.biz.convert.drink.TeaTypeMgtConvertor;
import com.langtuo.teamachine.dao.accessor.drink.TeaAccessor;
import com.langtuo.teamachine.dao.accessor.drink.TeaTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.TeaTypePO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.drink.TeaTypeMgtConvertor.convertToTeaTypePO;

@Component
@Slf4j
public class TeaTypeMgtServiceImpl implements TeaTypeMgtService {
    @Resource
    private TeaTypeAccessor accessor;

    @Resource
    private TeaAccessor teaAccessor;

    @Override
    public TeaMachineResult<List<TeaTypeDTO>> list(String tenantCode) {
        try {
            List<TeaTypePO> list = accessor.list(tenantCode);
            List<TeaTypeDTO> dtoList = TeaTypeMgtConvertor.convertToTeaTypePO(list);
            return TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("teaTypeMgtService|list|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<TeaTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<TeaTypePO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<TeaTypeDTO> dtoList = TeaTypeMgtConvertor.convertToTeaTypePO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("teaTypeMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<TeaTypeDTO> getByTeaTypeCode(String tenantCode, String toppingTypeCode) {
        try {
            TeaTypePO toppingTypePO = accessor.getByTeaTypeCode(tenantCode, toppingTypeCode);
            TeaTypeDTO tenantDTO = convertToTeaTypePO(toppingTypePO);
            return TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("teaTypeMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(TeaTypePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaTypePO teaTypePO = TeaTypeMgtConvertor.convertToTeaTypePO(request);
        if (request.isPutNew()) {
            return putNew(teaTypePO);
        } else {
            return putUpdate(teaTypePO);
        }
    }

    private TeaMachineResult<Void> putNew(TeaTypePO po) {
        try {TeaTypePO exist = accessor.getByTeaTypeCode(po.getTenantCode(), po.getTeaTypeCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = accessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("teaTypeMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("teaTypeMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(TeaTypePO po) {
        try {
            TeaTypePO exist = accessor.getByTeaTypeCode(po.getTenantCode(), po.getTeaTypeCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = accessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("teaTypeMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("teaTypeMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByTeaTypeCode(String tenantCode, String teaTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int countByTeaTypeCode = teaAccessor.countByTeaTypeCode(tenantCode, teaTypeCode);
            if (countByTeaTypeCode == CommonConsts.DB_SELECT_ZERO_ROW) {
                int deleted = accessor.deleteByTeaTypeCode(tenantCode, teaTypeCode);
                return TeaMachineResult.success();
            } else {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("teaTypeMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
