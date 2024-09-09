package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.SupplyActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.record.SupplyActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.SupplyActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.SupplyActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.record.SupplyActRecordPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;

@Component
@Slf4j
public class SupplyActRecordMgtServiceImpl implements SupplyActRecordMgtService {
    @Resource
    private SupplyActRecordAccessor accessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<SupplyActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<SupplyActRecordDTO> teaMachineResult;
        try {
            SupplyActRecordPO po = accessor.selectOne(tenantCode, idempotentMark);
            SupplyActRecordDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<SupplyActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<SupplyActRecordDTO>> teaMachineResult;
        try {
            if (CollectionUtils.isEmpty(shopGroupCodeList) && CollectionUtils.isEmpty(shopCodeList)) {
                List<ShopGroupDTO> shopGroupDTOList = getListModel(shopGroupMgtService.listByAdminOrg(tenantCode));
                if (!CollectionUtils.isEmpty(shopGroupDTOList)) {
                    shopGroupCodeList = shopGroupDTOList.stream()
                            .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                            .collect(Collectors.toList());
                }
            }

            PageInfo<SupplyActRecordPO> pageInfo = accessor.search(tenantCode, shopGroupCodeList,
                    shopCodeList, pageNum, pageSize);
            List<SupplyActRecordDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(SupplyActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        SupplyActRecordPO po = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            SupplyActRecordPO exist = accessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = accessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = accessor.insert(po);

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = accessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
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

        ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(po.getTenantCode(), po.getToppingCode());
        if (toppingPO != null) {
            dto.setToppingName(toppingPO.getToppingName());
        }
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByShopGroupCode(po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }
        ShopPO shopPO = shopAccessor.selectOneByShopCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopName(shopPO.getShopName());
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
