package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.record.InvalidActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.record.InvalidActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.dao.accessor.record.InvalidActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.LangTuoResult.getModel;

@Component
@Slf4j
public class InvalidActRecordMgtServiceImpl implements InvalidActRecordMgtService {
    @Resource
    private InvalidActRecordAccessor accessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Resource
    private ShopMgtService shopMgtService;

    @Override
    public LangTuoResult<InvalidActRecordDTO> get(String tenantCode, String idempotentMark) {
        LangTuoResult<InvalidActRecordDTO> langTuoResult = null;
        try {
            InvalidActRecordPO po = accessor.selectOne(tenantCode, idempotentMark);
            InvalidActRecordDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<InvalidActRecordDTO>> list(String tenantCode) {
        LangTuoResult<List<InvalidActRecordDTO>> langTuoResult = null;
        try {
            List<InvalidActRecordPO> poList = accessor.selectList(tenantCode);
            List<InvalidActRecordDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<InvalidActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<InvalidActRecordDTO>> langTuoResult = null;
        try {
            PageInfo<InvalidActRecordPO> pageInfo = accessor.search(tenantCode, shopGroupCode,
                    shopCode, pageNum, pageSize);
            List<InvalidActRecordDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(InvalidActRecordPutRequest request) {
        if (request == null) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        InvalidActRecordPO po = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            InvalidActRecordPO exist = accessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = accessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = accessor.insert(po);

            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        LangTuoResult<Void> langTuoResult = null;
        try {
            int deleted = accessor.delete(tenantCode, warningRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<InvalidActRecordDTO> convert(List<InvalidActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<InvalidActRecordDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private InvalidActRecordDTO convert(InvalidActRecordPO po) {
        if (po == null) {
            return null;
        }

        InvalidActRecordDTO dto = new InvalidActRecordDTO();
        dto.setExtraInfo(po.getExtraInfo());
        dto.setIdempotentMark(po.getIdempotentMark());
        dto.setMachineCode(po.getMachineCode());
        dto.setShopCode(po.getShopCode());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setInvalidTime(po.getInvalidTime());
        dto.setToppingCode(po.getToppingCode());
        dto.setPipelineNum(po.getPipelineNum());
        dto.setInvalidAmount(po.getInvalidAmount());

        ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(
                po.getTenantCode(), po.getToppingCode()));
        if (toppingDTO != null) {
            dto.setToppingName(toppingDTO.getToppingName());
        }
        ShopGroupDTO shopGroupDTO = getModel(shopGroupMgtService.getByCode(po.getTenantCode(), po.getShopGroupCode()));
        if (shopGroupDTO != null) {
            dto.setShopGroupName(shopGroupDTO.getShopGroupName());
        }
        ShopDTO shopDTO = getModel(shopMgtService.getByCode(po.getTenantCode(), po.getShopCode()));
        if (shopDTO != null) {
            dto.setShopName(shopDTO.getShopName());
        }
        return dto;
    }

    private InvalidActRecordPO convert(InvalidActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        InvalidActRecordPO po = new InvalidActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setInvalidTime(request.getInvalidTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setInvalidAmount(request.getInvalidAmount());
        return po;
    }
}
