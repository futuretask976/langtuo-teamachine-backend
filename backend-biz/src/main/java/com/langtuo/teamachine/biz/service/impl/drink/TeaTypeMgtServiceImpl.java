package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.TeaTypeDTO;
import com.langtuo.teamachine.api.request.drink.TeaTypePutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.TeaTypeMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.drink.TeaAccessor;
import com.langtuo.teamachine.dao.accessor.drink.TeaTypeAccessor;
import com.langtuo.teamachine.dao.po.drink.TeaTypePO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TeaTypeMgtServiceImpl implements TeaTypeMgtService {
    @Resource
    private TeaTypeAccessor accessor;

    @Resource
    private TeaAccessor teaAccessor;
    
    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<TeaTypeDTO>> list(String tenantCode) {
        TeaMachineResult<List<TeaTypeDTO>> teaMachineResult;
        try {
            List<TeaTypePO> list = accessor.selectList(tenantCode);
            List<TeaTypeDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<TeaTypeDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<TeaTypeDTO>> teaMachineResult;
        try {
            PageInfo<TeaTypePO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<TeaTypeDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<TeaTypeDTO> getByCode(String tenantCode, String toppingTypeCode) {
        TeaMachineResult<TeaTypeDTO> teaMachineResult;
        try {
            TeaTypePO toppingTypePO = accessor.selectOneByTeaTypeCode(tenantCode, toppingTypeCode);
            TeaTypeDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<TeaTypeDTO> getByName(String tenantCode, String toppingTypeName) {
        TeaMachineResult<TeaTypeDTO> teaMachineResult;
        try {
            TeaTypePO toppingTypePO = accessor.selectOneByTeaName(tenantCode, toppingTypeName);
            TeaTypeDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(TeaTypePutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaTypePO teaTypePO = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            TeaTypePO exist = accessor.selectOneByTeaTypeCode(teaTypePO.getTenantCode(), teaTypePO.getTeaTypeCode());
            if (exist != null) {
                int updated = accessor.update(teaTypePO);
            } else {
                int inserted = accessor.insert(teaTypePO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String teaTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int countByTeaTypeCode = teaAccessor.countByTeaTypeCode(tenantCode, teaTypeCode);
            if (countByTeaTypeCode == BizConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted = accessor.deleteByTeaTypeCode(tenantCode, teaTypeCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_TEA_TYPE));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<TeaTypeDTO> convert(List<TeaTypePO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<TeaTypeDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private TeaTypeDTO convert(TeaTypePO po) {
        if (po == null) {
            return null;
        }

        TeaTypeDTO dto = new TeaTypeDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setTeaTypeCode(po.getTeaTypeCode());
        dto.setTeaTypeName(po.getTeaTypeName());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());

        int teaCount = teaAccessor.countByTeaTypeCode(po.getTenantCode(), po.getTeaTypeCode());
        dto.setTeaCount(teaCount);

        return dto;
    }

    private TeaTypePO convert(TeaTypePutRequest request) {
        if (request == null) {
            return null;
        }

        TeaTypePO po = new TeaTypePO();
        po.setTeaTypeCode(request.getTeaTypeCode());
        po.setTeaTypeName(request.getTeaTypeName());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(request.getExtraInfo());
        return po;
    }
}
