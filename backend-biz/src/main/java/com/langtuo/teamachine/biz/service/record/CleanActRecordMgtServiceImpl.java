package com.langtuo.teamachine.biz.service.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.CleanActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.CleanActRecordMgtService;
import com.langtuo.teamachine.api.service.shop.ShopGroupMgtService;
import com.langtuo.teamachine.dao.accessor.drink.ToppingAccessor;
import com.langtuo.teamachine.dao.accessor.record.CleanActRecordAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopAccessor;
import com.langtuo.teamachine.dao.accessor.shop.ShopGroupAccessor;
import com.langtuo.teamachine.dao.po.record.CleanActRecordPO;
import com.langtuo.teamachine.dao.util.DaoUtils;
import com.langtuo.teamachine.internal.constant.CommonConsts;
import com.langtuo.teamachine.internal.constant.ErrorCodeEnum;
import com.langtuo.teamachine.internal.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.langtuo.teamachine.biz.convert.record.CleanActRecordMgtConvertor.convertToCleanActRecordDTO;

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

    @Override
    public TeaMachineResult<CleanActRecordDTO> get(String tenantCode, String idempotentMark) {
        try {
            CleanActRecordPO po = cleanActRecordAccessor.getByIdempotentMark(tenantCode, idempotentMark);
            return TeaMachineResult.success(convertToCleanActRecordDTO(po, true));
        } catch (Exception e) {
            log.error("cleanActRecordMgtService|get|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<CleanActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<CleanActRecordPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = cleanActRecordAccessor.searchByShopCode(tenantCode, Lists.newArrayList(shopCode),
                        pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = cleanActRecordAccessor.searchByShopGroupCode(tenantCode, Lists.newArrayList(shopGroupCode),
                        pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
                pageInfo = cleanActRecordAccessor.searchByShopGroupCode(tenantCode, shopGroupCodeList,
                        pageNum, pageSize);
            }

            if (pageInfo == null) {
                return TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                return TeaMachineResult.success(new PageDTO<>(
                        convertToCleanActRecordDTO(pageInfo.getList(), false),
                        pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("cleanActRecordMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String idempotentMark) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }
        
        try {
            int deleted = cleanActRecordAccessor.delete(tenantCode, idempotentMark);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("cleanActRecordMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
