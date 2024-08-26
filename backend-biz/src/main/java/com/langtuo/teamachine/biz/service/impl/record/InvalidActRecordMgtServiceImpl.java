package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.InvalidActRecordDTO;
import com.langtuo.teamachine.api.model.shop.ShopGroupDTO;
import com.langtuo.teamachine.api.request.record.InvalidActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.InvalidActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.InvalidActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.record.InvalidActRecordPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.langtuo.teamachine.api.result.TeaMachineResult.getListModel;

@Component
@Slf4j
public class InvalidActRecordMgtServiceImpl implements InvalidActRecordMgtService {
    @Resource
    private InvalidActRecordAccessor accessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopGroupMgtService shopGroupMgtService;

    @Override
    public TeaMachineResult<InvalidActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<InvalidActRecordDTO> teaMachineResult;
        try {
            InvalidActRecordPO po = accessor.selectOne(tenantCode, idempotentMark);
            InvalidActRecordDTO dto = convert(po);
            teaMachineResult = TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<InvalidActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<InvalidActRecordDTO>> teaMachineResult;
        try {
            if (CollectionUtils.isEmpty(shopGroupCodeList) && CollectionUtils.isEmpty(shopCodeList)) {
                List<ShopGroupDTO> shopGroupDTOList = getListModel(shopGroupMgtService.listByAdminOrg(tenantCode));
                shopGroupCodeList = shopGroupDTOList.stream()
                        .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                        .collect(Collectors.toList());
            }

            if (CollectionUtils.isEmpty(shopGroupCodeList) && CollectionUtils.isEmpty(shopCodeList)) {
                List<ShopGroupDTO> shopGroupDTOList = getListModel(shopGroupMgtService.listByAdminOrg(tenantCode));
                shopGroupCodeList = shopGroupDTOList.stream()
                        .map(shopGroupDTO -> shopGroupDTO.getShopGroupCode())
                        .collect(Collectors.toList());
            }

            PageInfo<InvalidActRecordPO> pageInfo = accessor.search(tenantCode, shopGroupCodeList,
                    shopCodeList, pageNum, pageSize);
            List<InvalidActRecordDTO> dtoList = convert(pageInfo.getList());
            teaMachineResult = TeaMachineResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_SELECT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(InvalidActRecordPutRequest request) {
        if (request == null) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        InvalidActRecordPO po = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            InvalidActRecordPO exist = accessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = accessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = accessor.insert(po);

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = accessor.delete(tenantCode, warningRuleCode);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ErrorEnum.DB_ERR_INSERT_FAIL);
        }
        return teaMachineResult;
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

        ToppingPO toppingPO = toppingAccessor.selectOneByToppingCode(
                po.getTenantCode(), po.getToppingCode());
        if (toppingPO != null) {
            dto.setToppingName(toppingPO.getToppingName());
        }
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOneByCode(po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }
        ShopPO shopPO = shopAccessor.selectOneByCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopName(shopPO.getShopName());
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
