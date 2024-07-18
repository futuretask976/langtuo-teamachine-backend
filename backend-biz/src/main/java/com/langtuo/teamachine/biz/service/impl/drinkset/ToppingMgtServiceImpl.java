package com.langtuo.teamachine.biz.service.impl.drinkset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drinkset.ToppingDTO;
import com.langtuo.teamachine.api.request.drinkset.ToppingPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drinkset.ToppingMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.ToppingAccessor;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToppingMgtServiceImpl implements ToppingMgtService {
    @Resource
    private ToppingAccessor accessor;

    @Override
    public LangTuoResult<List<ToppingDTO>> list(String tenantCode) {
        LangTuoResult<List<ToppingDTO>> langTuoResult = null;
        try {
            List<ToppingPO> list = accessor.selectList(tenantCode);
            List<ToppingDTO> dtoList = list.stream()
                    .map(po -> convert(po))
                    .collect(Collectors.toList());

            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<ToppingDTO>> search(String tenantName, String toppingTypeCode,
            String toppingTypeName, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<ToppingDTO>> langTuoResult = null;
        try {
            PageInfo<ToppingPO> pageInfo = accessor.search(tenantName, toppingTypeCode, toppingTypeName,
                    pageNum, pageSize);
            List<ToppingDTO> dtoList = pageInfo.getList().stream()
                    .map(po -> convert(po))
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
    public LangTuoResult<ToppingDTO> getByCode(String tenantCode, String toppingTypeCode) {
        LangTuoResult<ToppingDTO> langTuoResult = null;
        try {
            ToppingPO toppingTypePO = accessor.selectOneByCode(tenantCode, toppingTypeCode);
            ToppingDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<ToppingDTO> getByName(String tenantCode, String toppingTypeName) {
        LangTuoResult<ToppingDTO> langTuoResult = null;
        try {
            ToppingPO toppingTypePO = accessor.selectOneByName(tenantCode, toppingTypeName);
            ToppingDTO tenantDTO = convert(toppingTypePO);

            langTuoResult = LangTuoResult.success(tenantDTO);
        } catch (Exception e) {
            e.printStackTrace();
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(ToppingPutRequest toppingPutRequest) {
        if (toppingPutRequest == null
                || StringUtils.isBlank(toppingPutRequest.getTenantCode())
                || StringUtils.isBlank(toppingPutRequest.getToppingCode())
                || StringUtils.isBlank(toppingPutRequest.getToppingName())) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        ToppingPO toppingTypePO = convert(toppingPutRequest);

        LangTuoResult<Void> langTuoResult = null;
        try {
            ToppingPO exist = accessor.selectOneByCode(toppingTypePO.getTenantCode(),
                    toppingTypePO.getToppingCode());
            if (exist != null) {
                int updated = accessor.update(toppingTypePO);
            } else {
                int inserted = accessor.insert(toppingTypePO);
            }
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String toppingTypeCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, toppingTypeCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private ToppingDTO convert(ToppingPO po) {
        if (po == null) {
            return null;
        }

        ToppingDTO dto = new ToppingDTO();
        dto.setId(po.getId());
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
