package com.langtuo.teamachine.biz.service.impl.drink;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.request.drink.ToppingPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.mapper.drink.ToppingBaseRuleMapper;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
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
public class ToppingMgtServiceImpl implements ToppingMgtService {
    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private ToppingBaseRuleMapper toppingBaseRuleMapper;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<List<ToppingDTO>> list(String tenantCode) {
        TeaMachineResult<List<ToppingDTO>> teaMachineResult;
        try {
            List<ToppingPO> list = toppingAccessor.selectList(tenantCode);
            List<ToppingDTO> dtoList = convert(list);
            teaMachineResult = TeaMachineResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<ToppingDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<ToppingDTO>> teaMachineResult;
        try {
            PageInfo<ToppingPO> pageInfo = toppingAccessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(),
                    pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<ToppingDTO> getByCode(String tenantCode, String toppingTypeCode) {
        TeaMachineResult<ToppingDTO> teaMachineResult;
        try {
            ToppingPO toppingTypePO = toppingAccessor.selectOneByToppingCode(tenantCode, toppingTypeCode);
            ToppingDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<ToppingDTO> getByName(String tenantCode, String toppingTypeName) {
        TeaMachineResult<ToppingDTO> teaMachineResult;
        try {
            ToppingPO toppingTypePO = toppingAccessor.selectOneByToppingName(tenantCode, toppingTypeName);
            ToppingDTO tenantDTO = convert(toppingTypePO);
            teaMachineResult = TeaMachineResult.success(tenantDTO);
        } catch (Exception e) {
            log.error("getByName error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(ToppingPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        ToppingPO toppingTypePO = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            ToppingPO exist = toppingAccessor.selectOneByToppingCode(toppingTypePO.getTenantCode(),
                    toppingTypePO.getToppingCode());
            if (exist != null) {
                int updated = toppingAccessor.update(toppingTypePO);
            } else {
                int inserted = toppingAccessor.insert(toppingTypePO);
            }
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String toppingCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int countByToppingCode = toppingBaseRuleMapper.countByToppingCode(tenantCode, toppingCode);
            if (countByToppingCode == BizConsts.DB_SELECT_RESULT_EMPTY) {
                int deleted = toppingAccessor.deleteByToppingCode(tenantCode, toppingCode);
                teaMachineResult = TeaMachineResult.success();
            } else {
                teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_CANNOT_DELETE_USING_TOPPING));
            }
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
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
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<ToppingDTO> convert(List<ToppingPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<ToppingDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private ToppingDTO convert(ToppingPO po) {
        if (po == null) {
            return null;
        }

        ToppingDTO dto = new ToppingDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setToppingTypeCode(po.getToppingTypeCode());
        dto.setToppingCode(po.getToppingCode());
        dto.setToppingName(po.getToppingName());
        dto.setState(po.getState());
        dto.setValidHourPeriod(po.getValidHourPeriod());
        dto.setCleanHourPeriod(po.getCleanHourPeriod());
        dto.setMeasureUnit(po.getMeasureUnit());
        dto.setConvertCoefficient(po.getConvertCoefficient());
        dto.setFlowSpeed(po.getFlowSpeed());
        dto.setComment(po.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return dto;
    }

    private ToppingPO convert(ToppingPutRequest request) {
        if (request == null) {
            return null;
        }

        ToppingPO po = new ToppingPO();
        po.setToppingTypeCode(request.getToppingTypeCode());
        po.setToppingName(request.getToppingName());
        po.setToppingCode(request.getToppingCode());
        po.setState(request.getState());
        po.setValidHourPeriod(request.getValidHourPeriod());
        po.setCleanHourPeriod(request.getCleanHourPeriod());
        po.setMeasureUnit(request.getMeasureUnit());
        po.setConvertCoefficient(request.getConvertCoefficient());
        po.setFlowSpeed(request.getFlowSpeed());
        po.setTenantCode(request.getTenantCode());
        po.setComment(request.getComment());
        po.setExtraInfo(po.getExtraInfo());
        return po;
    }
}
