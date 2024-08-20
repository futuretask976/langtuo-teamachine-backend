package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.drink.ToppingDTO;
import com.langtuo.teamachine.api.model.record.SupplyActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.record.SupplyActRecordPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.drink.ToppingMgtService;
import com.langtuo.teamachine.api.service.record.SupplyActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.api.service.shop.ShopMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.record.SupplyActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
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
public class SupplyActRecordMgtServiceImpl implements SupplyActRecordMgtService {
    @Resource
    private SupplyActRecordAccessor accessor;

    @Resource
    private ToppingMgtService toppingMgtService;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Resource
    private ShopMgtService shopMgtService;

    @Override
    public LangTuoResult<SupplyActRecordDTO> get(String tenantCode, String idempotentMark) {
        LangTuoResult<SupplyActRecordDTO> langTuoResult;
        try {
            SupplyActRecordPO po = accessor.selectOne(tenantCode, idempotentMark);
            SupplyActRecordDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<SupplyActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        LangTuoResult<PageDTO<SupplyActRecordDTO>> langTuoResult;
        try {
            PageInfo<SupplyActRecordPO> pageInfo = accessor.search(tenantCode, shopGroupCodeList,
                    shopCodeList, pageNum, pageSize);
            List<SupplyActRecordDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(SupplyActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        SupplyActRecordPO po = convert(request);

        LangTuoResult<Void> langTuoResult;
        try {
            SupplyActRecordPO exist = accessor.selectOne(po.getTenantCode(),
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

        LangTuoResult<Void> langTuoResult;
        try {
            int deleted = accessor.delete(tenantCode, warningRuleCode);
            langTuoResult = LangTuoResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return langTuoResult;
    }

    private List<SupplyActRecordDTO> convert(List<SupplyActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<SupplyActRecordDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private SupplyActRecordDTO convert(SupplyActRecordPO po) {
        if (po == null) {
            return null;
        }

        SupplyActRecordDTO dto = new SupplyActRecordDTO();
        dto.setExtraInfo(po.getExtraInfo());
        dto.setIdempotentMark(po.getIdempotentMark());
        dto.setMachineCode(po.getMachineCode());
        dto.setShopCode(po.getShopCode());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setSupplyTime(po.getSupplyTime());
        dto.setToppingCode(po.getToppingCode());
        dto.setPipelineNum(po.getPipelineNum());
        dto.setSupplyAmount(po.getSupplyAmount());

        ToppingDTO toppingDTO = getModel(toppingMgtService.getByCode(po.getTenantCode(), po.getToppingCode()));
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

    private SupplyActRecordPO convert(SupplyActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        SupplyActRecordPO po = new SupplyActRecordPO();
        po.setTenantCode(request.getTenantCode());
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setSupplyTime(request.getSupplyTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setSupplyAmount(request.getSupplyAmount());
        return po;
    }
}
