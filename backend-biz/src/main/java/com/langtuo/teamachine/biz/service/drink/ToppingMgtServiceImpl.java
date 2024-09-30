package com.langtuo.teamachine.biz.service.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.request.drink.ToppingPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.biz.convert.drink.ToppingMgtConvertor;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.mapper.drink.ToppingBaseRuleMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.drink.ToppingMgtConvertor.convertToToppingDTO;

@Component
@Slf4j
public class ToppingMgtServiceImpl implements ToppingMgtService {
    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private ToppingBaseRuleMapper toppingBaseRuleMapper;

    @Override
    public TeaMachineResult<List<ToppingDTO>> list(String tenantCode) {
        TeaMachineResult<List<ToppingDTO>> teaMachineResult;
        try {
            List<ToppingPO> list = toppingAccessor.list(tenantCode);
            List<ToppingDTO> dtoList = ToppingMgtConvertor.convertToToppingDTO(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("toppingMgtService|list|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<ToppingDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<ToppingPO> pageInfo = toppingAccessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingDTO> dtoList = ToppingMgtConvertor.convertToToppingDTO(pageInfo.getList());
            return TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("toppingMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<ToppingDTO> getByToppingCode(String tenantCode, String toppingTypeCode) {
        try {
            ToppingPO toppingTypePO = toppingAccessor.getByToppingCode(tenantCode, toppingTypeCode);
            ToppingDTO tenantDTO = ToppingMgtConvertor.convertToToppingDTO(toppingTypePO);
            return TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("toppingMgtService|getByCode|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> put(ToppingPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ToppingPO toppingTypePO = convertToToppingDTO(request);
        if (request.isPutNew()) {
            return putNew(toppingTypePO);
        } else {
            return putUpdate(toppingTypePO);
        }
    }

    private TeaMachineResult<Void> putNew(ToppingPO po) {
        try {
            ToppingPO exist = toppingAccessor.getByToppingCode(po.getTenantCode(), po.getToppingCode());
            if (exist != null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_CODE_DUPLICATED));
            }

            int inserted = toppingAccessor.insert(po);
            if (CommonConsts.DB_INSERTED_ONE_ROW != inserted) {
                log.error("toppingMgtService|putNew|error|" + inserted);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("toppingMgtService|putNew|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    private TeaMachineResult<Void> putUpdate(ToppingPO po) {
        try {
            ToppingPO exist = toppingAccessor.getByToppingCode(po.getTenantCode(), po.getToppingCode());
            if (exist == null) {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_OBJECT_NOT_FOUND));
            }

            int updated = toppingAccessor.update(po);
            if (CommonConsts.DB_UPDATED_ONE_ROW != updated) {
                log.error("toppingMgtService|putUpdate|error|" + updated);
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
            }
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("toppingMgtService|putUpdate|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_UPDATE_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> deleteByToppingCode(String tenantCode, String toppingCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int countByToppingCode = toppingBaseRuleMapper.countByToppingCode(tenantCode, toppingCode);
            if (countByToppingCode == CommonConsts.DB_SELECT_ZERO_ROW) {
                int deleted = toppingAccessor.deleteByToppingCode(tenantCode, toppingCode);
                return TeaMachineResult.success();
            } else {
                return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(
                        ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_OBJECT));
            }
        } catch (Exception e) {
            log.error("toppingMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Integer> countByToppingTypeCode(String tenantCode, String toppingTypeCode) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(toppingTypeCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Integer> teaMachineResult;
        try {
            int cnt = toppingAccessor.countByToppingTypeCode(tenantCode, toppingTypeCode);
            teaMachineResult = TeaMachineResult.success(cnt);
        } catch (Exception e) {
            log.error("toppingMgtService|countByToppingTypeCode|fatal|" + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }
}
