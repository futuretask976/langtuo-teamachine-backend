package com.langtuo.teamachine.biz.service.impl.recordset;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.constant.ErrorEnum;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.recordset.CleanActRecordDTO;
import com.langtuo.teamachine.api.request.recordset.CleanActRecordPutRequest;
import com.langtuo.teamachine.api.result.LangTuoResult;
import com.langtuo.teamachine.api.service.recordset.CleanActRecordMgtService;
import com.langtuo.teamachine.dao.accessor.drinkset.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.recordset.CleanActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shopset.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shopset.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.drinkset.ToppingPO;
import com.langtuo.teamachine.dao.po.recordset.CleanActRecordPO;
import com.langtuo.teamachine.dao.po.shopset.ShopGroupPO;
import com.langtuo.teamachine.dao.po.shopset.ShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CleanActRecordMgtServiceImpl implements CleanActRecordMgtService {
    @Resource
    private CleanActRecordAccessor accessor;

    @Resource
    private ToppingAccessor toppingAccessor;

    @Resource
    private ShopGroupAccessor shopGroupAccessor;

    @Resource
    private ShopAccessor shopAccessor;

    @Override
    public LangTuoResult<CleanActRecordDTO> get(String tenantCode, String idempotentMark) {
        LangTuoResult<CleanActRecordDTO> langTuoResult = null;
        try {
            CleanActRecordPO po = accessor.selectOne(tenantCode, idempotentMark);
            CleanActRecordDTO dto = convert(po);
            langTuoResult = LangTuoResult.success(dto);
        } catch (Exception e) {
            log.error("getByCode error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<List<CleanActRecordDTO>> list(String tenantCode) {
        LangTuoResult<List<CleanActRecordDTO>> langTuoResult = null;
        try {
            List<CleanActRecordPO> poList = accessor.selectList(tenantCode);
            List<CleanActRecordDTO> dtoList = convert(poList);
            langTuoResult = LangTuoResult.success(dtoList);
        } catch (Exception e) {
            log.error("list error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<PageDTO<CleanActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <=0 ? 20 : pageSize;

        LangTuoResult<PageDTO<CleanActRecordDTO>> langTuoResult = null;
        try {
            PageInfo<CleanActRecordPO> pageInfo = accessor.search(tenantCode, shopGroupCode,
                    shopCode, pageNum, pageSize);
            List<CleanActRecordDTO> dtoList = convert(pageInfo.getList());
            langTuoResult = LangTuoResult.success(new PageDTO<>(dtoList, pageInfo.getTotal(), pageNum, pageSize));
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            langTuoResult = LangTuoResult.error(ErrorEnum.DB_ERR_QUERY_FAIL);
        }
        return langTuoResult;
    }

    @Override
    public LangTuoResult<Void> put(CleanActRecordPutRequest request) {
        if (request == null || !request.isValid()) {
            return LangTuoResult.error(ErrorEnum.BIZ_ERR_ILLEGAL_ARGUMENT);
        }

        CleanActRecordPO po = convert(request);

        LangTuoResult<Void> langTuoResult = null;
        try {
            CleanActRecordPO exist = accessor.selectOne(po.getTenantCode(),
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

    private List<CleanActRecordDTO> convert(List<CleanActRecordPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        List<CleanActRecordDTO> list = poList.stream()
                .map(po -> convert(po))
                .collect(Collectors.toList());
        return list;
    }

    private CleanActRecordDTO convert(CleanActRecordPO po) {
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
        dto.setOpenRuleCode(po.getOpenRuleCode());
        dto.setCloseRuleCode(po.getCloseRuleCode());
        dto.setCleanContent(po.getCleanContent());
        dto.setWashSec(po.getWashSec());
        dto.setSoakMin(po.getSoakMin());
        dto.setFlushSec(po.getFlushSec());
        dto.setFlushIntervalMin(po.getFlushIntervalMin());

        ToppingPO toppingPO = toppingAccessor.selectOneByCode(po.getTenantCode(), po.getToppingCode());
        if (toppingPO != null) {
            dto.setToppingName(toppingPO.getToppingName());
        }
        ShopGroupPO shopGroupPO = shopGroupAccessor.selectOne(po.getTenantCode(), po.getShopGroupCode());
        if (shopGroupPO != null) {
            dto.setShopGroupName(shopGroupPO.getShopGroupName());
        }
        ShopPO shopPO = shopAccessor.selectOneByCode(po.getTenantCode(), po.getShopCode());
        if (shopPO != null) {
            dto.setShopName(shopPO.getShopName());
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
        po.setOpenRuleCode(request.getOpenRuleCode());
        po.setCloseRuleCode(request.getCloseRuleCode());
        po.setCleanContent(request.getCleanContent());
        po.setWashSec(request.getWashSec());
        po.setSoakMin(request.getSoakMin());
        po.setFlushSec(request.getFlushSec());
        po.setFlushIntervalMin(request.getFlushIntervalMin());
        return po;
    }
}
