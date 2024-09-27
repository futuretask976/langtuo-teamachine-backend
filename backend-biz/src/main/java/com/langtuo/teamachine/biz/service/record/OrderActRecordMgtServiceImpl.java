package com.langtuo.teamachine.biz.service.record;

import com.github.pagehelper.PageInfo;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.record.OrderActRecordDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.record.OrderActRecordMgtService;
import com.langtuo.teamachine.dao.accessor.record.OrderActRecordAccessor;
import com.langtuo.teamachine.dao.po.record.OrderActRecordPO;
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

import static com.langtuo.teamachine.biz.convert.record.OrderActRecordMgtConvertor.convertToOrderActRecordDTO;

@Component
@Slf4j
public class OrderActRecordMgtServiceImpl implements OrderActRecordMgtService {
    @Resource
    private OrderActRecordAccessor orderActRecordAccessor;

    @Override
    public TeaMachineResult<OrderActRecordDTO> get(String tenantCode, String idempotentMark) {
        try {
            OrderActRecordPO po = orderActRecordAccessor.getByIdempotentMark(tenantCode, idempotentMark);
            OrderActRecordDTO dto = convertToOrderActRecordDTO(po, true);
            return TeaMachineResult.success(dto);
        } catch (Exception e) {
            log.error("orderActRecordMgtService|get|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<PageDTO<OrderActRecordDTO>> search(String tenantCode, String shopGroupCode,
            String shopCode, int pageNum, int pageSize) {
        pageNum = pageNum < CommonConsts.MIN_PAGE_NUM ? CommonConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < CommonConsts.MIN_PAGE_SIZE ? CommonConsts.MIN_PAGE_SIZE : pageSize;

        try {
            PageInfo<OrderActRecordPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = orderActRecordAccessor.searchByShopCode(tenantCode, Lists.newArrayList(shopCode),
                        pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = orderActRecordAccessor.searchByShopGroupCode(tenantCode, Lists.newArrayList(shopGroupCode),
                        pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = DaoUtils.getShopGroupCodeListByLoginSession(tenantCode);
                pageInfo = orderActRecordAccessor.searchByShopGroupCode(tenantCode, shopGroupCodeList,
                        pageNum, pageSize);
            }

            if (pageInfo == null) {
                return TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                return TeaMachineResult.success(new PageDTO<>(
                        convertToOrderActRecordDTO(pageInfo.getList(), false), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("orderActRecordMgtService|search|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL));
        }
    }

    @Override
    public TeaMachineResult<Void> delete(String tenantCode, String warningRuleCode) {
        if (StringUtils.isEmpty(tenantCode)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT));
        }

        try {
            int deleted = orderActRecordAccessor.delete(tenantCode, warningRuleCode);
            return TeaMachineResult.success();
        } catch (Exception e) {
            log.error("orderActRecordMgtService|delete|fatal|" + e.getMessage(), e);
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_INSERT_FAIL));
        }
    }
}
