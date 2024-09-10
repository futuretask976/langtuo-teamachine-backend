package com.langtuo.teamachine.biz.service.impl.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.CleanActRecordDTO;
import com.langtuo.teamachine.api.request.record.CleanActRecordPutRequest;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.CleanActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.util.ApiUtils;
import com.langtuo.teamachine.biz.service.util.BizUtils;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.CleanActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drink.ToppingPO;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
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

@Component
@Slf4j
public class CleanActRecordMgtServiceImpl implements CleanActRecordMgtService {
    @Resource
    private CleanActRecordAccessor cleanActRecordAccessor;

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
    public TeaMachineResult<CleanActRecordDTO> get(String tenantCode, String idempotentMark) {
        TeaMachineResult<CleanActRecordDTO> teaMachineResult;
        try {
            CleanActRecordPO po = cleanActRecordAccessor.selectOne(tenantCode, idempotentMark);
            teaMachineResult = TeaMachineResult.success(convert(po, true));
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<CleanActRecordDTO>> search(String tenantCode, List<String> shopGroupCodeList,
            List<String> shopCodeList, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<CleanActRecordDTO>> teaMachineResult;
        try {
            if (CollectionUtils.isEmpty(shopCodeList)) {
                if (CollectionUtils.isEmpty(shopGroupCodeList)) {
                    shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                }
                shopCodeList = BizUtils.getShopCodeListByShopGroupCode(tenantCode, shopGroupCodeList);
            }

            if (CollectionUtils.isEmpty(shopCodeList)) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                PageInfo<CleanActRecordPO> pageInfo = cleanActRecordAccessor.search(
                        tenantCode, shopCodeList, pageNum, pageSize);
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convert(pageInfo.getList(), false), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> put(CleanActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        CleanActRecordPO po = convert(request);

        TeaMachineResult<Void> teaMachineResult;
        try {
            CleanActRecordPO exist = cleanActRecordAccessor.selectOne(po.getTenantCode(),
                    po.getIdempotentMark());
            if (exist != null) {
                int updated = cleanActRecordAccessor.delete(po.getTenantCode(), po.getIdempotentMark());
            }
            int inserted = cleanActRecordAccessor.insert(po);

            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("put error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String idempotentMark) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        TeaMachineResult<Void> teaMachineResult;
        try {
            int deleted = cleanActRecordAccessor.delete(tenantCode, idempotentMark);
            teaMachineResult = TeaMachineResult.success();
        } catch (Exception e) {
            log.error("delete error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(ApiUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    private List<CleanActRecordDTO> convert(List<CleanActRecordPO> poList, boolean fillDetail) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CleanActRecordDTO> list = poList.stream()
                .map(po -> convert(po, fillDetail))
                .collect(Collectors.toList());
        return list;
    }

    private CleanActRecordDTO convert(CleanActRecordPO po, boolean fillDetail) {
        if (po == null) {
            return null;
        }

        CleanActRecordDTO dto = new CleanActRecordDTO();
        dto.setExtraInfo(po.getExtraInfo());
        dto.setIdempotentMark(po.getIdempotentMark());
        dto.setMachineCode(po.getMachineCode());
        dto.setShopCode(po.getShopCode());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setCleanStartTime(po.getCleanStartTime());
        dto.setCleanEndTime(po.getCleanEndTime());
        dto.setToppingCode(po.getToppingCode());
        dto.setPipelineNum(po.getPipelineNum());
        dto.setCleanType(po.getCleanType());
        dto.setCleanRuleCode(po.getCleanRuleCode());
        dto.setCleanContent(po.getCleanContent());
        dto.setWashSec(po.getWashSec());
        dto.setSoakMin(po.getSoakMin());
        dto.setFlushSec(po.getFlushSec());
        dto.setFlushIntervalMin(po.getFlushIntervalMin());

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

    private CleanActRecordPO convert(CleanActRecordPutRequest request) {
        if (request == null) {
            return null;
        }

        CleanActRecordPO po = new CleanActRecordPO();
        po.setExtraInfo(request.getExtraInfo());
        po.setIdempotentMark(request.getIdempotentMark());
        po.setMachineCode(request.getMachineCode());
        po.setShopCode(request.getShopCode());
        po.setShopGroupCode(request.getShopGroupCode());
        po.setCleanStartTime(request.getCleanStartTime());
        po.setCleanEndTime(request.getCleanEndTime());
        po.setToppingCode(request.getToppingCode());
        po.setPipelineNum(request.getPipelineNum());
        po.setCleanType(request.getCleanType());
        po.setCleanRuleCode(request.getCleanRuleCode());
        po.setCleanContent(request.getCleanContent());
        po.setWashSec(request.getWashSec());
        po.setSoakMin(request.getSoakMin());
        po.setFlushSec(request.getFlushSec());
        po.setFlushIntervalMin(request.getFlushIntervalMin());
        return po;
    }
}
