package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.DrainActRecordDTO;
import com.langtuo.teamachine.api.request.record.DrainActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.DrainActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.biz.service.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.DrainActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.po.record.DrainActRecordPO;
import com.langtuo.teamachine.dao.po.shop.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shop.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DrainActRecordMgtServiceImpl implements DrainActRecordMgtService {
    @Resource
    private DrainActRecordAccessor drainActRecordAccessor;

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
    public TeaMachineResult<DrainActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<DrainActRecordDTO> teaMachineResult;
        try {
            DrainActRecordPO po = drainActRecordAccessor.selectOne(tenantCode, idempotentMark);
            teaMachineResult = TeaMachineResult.success(convert(po, true));
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<DrainActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<DrainActRecordDTO>> teaMachineResult;
        try {
            PageInfo<DrainActRecordPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = drainActRecordAccessor.searchByShopCode(tenantCode, Lists.newArrayList(shopCode),
                        pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = drainActRecordAccessor.searchByShopGroupCode(tenantCode, Lists.newArrayList(shopGroupCode),
                        pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                pageInfo = drainActRecordAccessor.searchByShopGroupCode(tenantCode, shopGroupCodeList,
                        pageNum, pageSize);
            }

            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convert(pageInfo.getList(), false), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(DrainActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        DrainActRecordPO po = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            DrainActRecordPO exist = drainActRecordAccessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = drainActRecordAccessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = drainActRecordAccessor.insert(po);

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String idempotentMark) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = drainActRecordAccessor.delete(tenantCode, idempotentMark);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
        return teaMachineResult;
    }

    private List<DrainActRecordDTO> convert(List<DrainActRecordPO> poList, boolean fillDetail) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<DrainActRecordDTO> list = poList.stream()
                .map(po -> convert(po, fillDetail))
                .collect(Collectors.toList());
        return list;
    }

    private DrainActRecordDTO convert(DrainActRecordPO po, boolean fillDetail) {
        if (po == null) {
            return null;
        }

        DrainActRecordDTO dto = new DrainActRecordDTO();
        dto.setExtraInfo(po.getExtraInfo());
        dto.setIdempotentMark(po.getIdempotentMark());
        dto.setMachineCode(po.getMachineCode());
        dto.setShopCode(po.getShopCode());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setDrainStartTime(po.getDrainStartTime());
        dto.setDrainEndTime(po.getDrainEndTime());
        dto.setToppingCode(po.getToppingCode());
        dto.setPipelineNum(po.getPipelineNum());
        dto.setDrainType(po.getDrainType());
        dto.setDrainRuleCode(po.getDrainRuleCode());
        dto.setFlushSec(po.getFlushSec());
        dto.setFlushWeight(po.getFlushWeight());

        if (fillDetail) {
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
        }
        return dto;
    }

    private DrainActRecordPO convert(DrainActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        DrainActRecordPO po = new DrainActRecordPO();
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setDrainStartTime(request.getDrainStartTime());
        po.setDrainEndTime(request.getDrainEndTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setDrainType(request.getDrainType());
        po.setDrainRuleCode(request.getDrainRuleCode());
        po.setFlushSec(request.getFlushSec());
        po.setFlushWeight(request.getFlushWeight());
        return po;
    }
}
