package com.langtuo.teamachine.biz.service.impl.report;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.langtuo.teamachine.api.model.PageDTO;
import com.langtuo.teamachine.api.model.report.OrderAmtReportDTO;
import com.langtuo.teamachine.api.model.report.OrderSpecItemReportByShopDTO;
import com.langtuo.teamachine.api.model.report.OrderTeaReportByShopDTO;
import com.langtuo.teamachine.api.model.report.OrderToppingReportByShopDTO;
import com.langtuo.teamachine.api.result.TeaMachineResult;
import com.langtuo.teamachine.api.service.report.OrderReportService;
import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.biz.service.constant.BizConsts;
import com.langtuo.teamachine.biz.service.constant.ErrorCodeEnum;
import com.langtuo.teamachine.biz.service.util.BizUtils;
import com.langtuo.teamachine.biz.service.util.DateUtils;
import com.langtuo.teamachine.biz.service.util.MessageUtils;
import com.langtuo.teamachine.dao.accessor.report.OrderAmtReportAccessor;
import com.langtuo.teamachine.dao.accessor.report.OrderSpecItemReportByShopAccessor;
import com.langtuo.teamachine.dao.accessor.report.OrderTeaReportByShopAccessor;
import com.langtuo.teamachine.dao.accessor.report.OrderToppingReportByShopAccessor;
import com.langtuo.teamachine.dao.po.report.OrderAmtReportPO;
import com.langtuo.teamachine.dao.po.report.OrderSpecItemReportByShopPO;
import com.langtuo.teamachine.dao.po.report.OrderTeaReportByShopPO;
import com.langtuo.teamachine.dao.po.report.OrderToppingReportByShopPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OrderReportServiceImpl implements OrderReportService {
    @Resource
    private OrderAmtReportAccessor orderAmtReportAccessor;

    @Resource
    private OrderSpecItemReportByShopAccessor orderSpecItemReportByShopAccessor;

    @Resource
    private OrderTeaReportByShopAccessor orderTeaReportByShopAccessor;

    @Resource
    private OrderToppingReportByShopAccessor orderToppingReportByShopAccessor;

    @Autowired
    private MessageSource messageSource;

    @Override
    public TeaMachineResult<Void> calc(String tenantCode, String orderCreatedDay) {
        if (StringUtils.isBlank(tenantCode) || StringUtils.isBlank(orderCreatedDay)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        String curDay = DateUtils.transformYYYYMMDD(Calendar.getInstance().getTime());
        if (curDay.equals(orderCreatedDay)) {
            return TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.BIZ_ERR_ILLEGAL_ARGUMENT,
                    messageSource));
        }

        OrderAmtReportPO existAmtReport = orderAmtReportAccessor.selectOne(tenantCode, orderCreatedDay);
        if (existAmtReport != null) {
            int deleted = orderAmtReportAccessor.delete(tenantCode, orderCreatedDay);
        }
        OrderAmtReportPO amtReportPO = orderAmtReportAccessor.calcOne(tenantCode, orderCreatedDay);
        int inserted4OrderAmtReport = orderAmtReportAccessor.insert(amtReportPO);

        List<OrderTeaReportByShopPO> existTeaReportList = orderTeaReportByShopAccessor.selectListByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(existTeaReportList)) {
            int deleted = orderTeaReportByShopAccessor.delete(tenantCode, orderCreatedDay);
        }
        List<OrderTeaReportByShopPO> teaReportByShopPOList = orderTeaReportByShopAccessor.calcByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(teaReportByShopPOList)) {
            teaReportByShopPOList.forEach(orderTeaReportByShopPO -> {
                int inserted = orderTeaReportByShopAccessor.insert(orderTeaReportByShopPO);
            });
        }

        List<OrderSpecItemReportByShopPO> existSpecItemReportList = orderSpecItemReportByShopAccessor.selectListByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(existSpecItemReportList)) {
            int deleted = orderSpecItemReportByShopAccessor.delete(tenantCode, orderCreatedDay);
        }
        List<OrderSpecItemReportByShopPO> specItemReportByShopPOList = orderSpecItemReportByShopAccessor.calcByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(specItemReportByShopPOList)) {
            specItemReportByShopPOList.forEach(orderSpecItemReportByShopPO -> {
                int inserted = orderSpecItemReportByShopAccessor.insert(orderSpecItemReportByShopPO);
            });
        }

        List<OrderToppingReportByShopPO> existToppingReportList = orderToppingReportByShopAccessor.selectListByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(existSpecItemReportList)) {
            int deleted = orderSpecItemReportByShopAccessor.delete(tenantCode, orderCreatedDay);
        }
        List<OrderToppingReportByShopPO> toppingReportByShopPOList = orderToppingReportByShopAccessor.calcByDay(
                tenantCode, orderCreatedDay);
        if (!CollectionUtils.isEmpty(toppingReportByShopPOList)) {
            toppingReportByShopPOList.forEach(orderToppingReportByShopPO -> {
                int inserted = orderToppingReportByShopAccessor.insert(orderToppingReportByShopPO);
            });
        }

        return TeaMachineResult.success();
    }

    @Override
    public TeaMachineResult<PageDTO<OrderAmtReportDTO>> searchAmtReport(String tenantCode,
            String orderCreatedDay, int pageNum, int pageSize) {
        pageNum = pageNum < BizConsts.MIN_PAGE_NUM ? BizConsts.MIN_PAGE_NUM : pageNum;
        pageSize = pageSize < BizConsts.MIN_PAGE_SIZE ? BizConsts.MIN_PAGE_SIZE : pageSize;

        TeaMachineResult<PageDTO<OrderAmtReportDTO>> teaMachineResult;
        try {
            PageInfo<OrderAmtReportPO> pageInfo = orderAmtReportAccessor.search(tenantCode,
                    Lists.newArrayList(orderCreatedDay), pageNum, pageSize);
            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convertToOrderAmtReportDTO(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
            }

        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrderTeaReportByShopDTO>> searchTeaReportByShop(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize) {
        TeaMachineResult<PageDTO<OrderTeaReportByShopDTO>> teaMachineResult;
        try {
            PageInfo<OrderTeaReportByShopPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = orderTeaReportByShopAccessor.searchByShopCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopCode), pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = orderTeaReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopGroupCode), pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                pageInfo = orderTeaReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), shopGroupCodeList, pageNum, pageSize);
            }

            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convertToOrderTeaReportByShopDTO(pageInfo.getList()), pageInfo.getTotal(), pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrderSpecItemReportByShopDTO>> searchSpecItemReportByShop(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize) {
        TeaMachineResult<PageDTO<OrderSpecItemReportByShopDTO>> teaMachineResult;
        try {
            PageInfo<OrderSpecItemReportByShopPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = orderSpecItemReportByShopAccessor.searchByShopCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopCode), pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = orderSpecItemReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopGroupCode), pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                pageInfo = orderSpecItemReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), shopGroupCodeList, pageNum, pageSize);
            }

            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convertToOrderSpecItemReportByShopDTO(pageInfo.getList()), pageInfo.getTotal(),
                        pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    @Override
    public TeaMachineResult<PageDTO<OrderToppingReportByShopDTO>> searchToppingReportByShop(String tenantCode,
            String orderCreatedDay, String shopGroupCode, String shopCode, int pageNum, int pageSize) {
        TeaMachineResult<PageDTO<OrderToppingReportByShopDTO>> teaMachineResult;
        try {
            PageInfo<OrderToppingReportByShopPO> pageInfo = null;
            if (!StringUtils.isBlank(shopCode)) {
                pageInfo = orderToppingReportByShopAccessor.searchByShopCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopCode), pageNum, pageSize);
            } else if (!StringUtils.isBlank(shopGroupCode)) {
                pageInfo = orderToppingReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), Lists.newArrayList(shopGroupCode), pageNum, pageSize);
            } else {
                List<String> shopGroupCodeList = BizUtils.getShopGroupCodeListByAdmin(tenantCode);
                pageInfo = orderToppingReportByShopAccessor.searchByShopGroupCode(tenantCode,
                        Lists.newArrayList(orderCreatedDay), shopGroupCodeList, pageNum, pageSize);
            }

            if (pageInfo == null) {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        null, 0, pageNum, pageSize));
            } else {
                teaMachineResult = TeaMachineResult.success(new PageDTO<>(
                        convertToOrderToppingReportByShopDTO(pageInfo.getList()), pageInfo.getTotal(),
                        pageNum, pageSize));
            }
        } catch (Exception e) {
            log.error("search error: " + e.getMessage(), e);
            teaMachineResult = TeaMachineResult.error(MessageUtils.getErrorMsgDTO(ErrorCodeEnum.DB_ERR_SELECT_FAIL,
                    messageSource));
        }
        return teaMachineResult;
    }

    private List<OrderAmtReportDTO> convertToOrderAmtReportDTO(List<OrderAmtReportPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convertToOrderAmtReportDTO(po))
                .collect(Collectors.toList());
    }

    private OrderAmtReportDTO convertToOrderAmtReportDTO(OrderAmtReportPO po) {
        if (po == null) {
            return null;
        }

        OrderAmtReportDTO dto = new OrderAmtReportDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setOrderCreatedDay(po.getOrderCreatedDay());
        dto.setAmount(po.getAmount());
        return dto;
    }

    private List<OrderTeaReportByShopDTO> convertToOrderTeaReportByShopDTO(List<OrderTeaReportByShopPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convertToOrderTeaReportByShopDTO(po))
                .collect(Collectors.toList());
    }

    private OrderTeaReportByShopDTO convertToOrderTeaReportByShopDTO(OrderTeaReportByShopPO po) {
        if (po == null) {
            return null;
        }

        OrderTeaReportByShopDTO dto = new OrderTeaReportByShopDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setOrderCreatedDay(po.getOrderCreatedDay());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setShopCode(po.getShopCode());
        dto.setTeaCode(po.getTeaCode());
        dto.setAmount(po.getAmount());
        return dto;
    }

    private List<OrderSpecItemReportByShopDTO> convertToOrderSpecItemReportByShopDTO(
            List<OrderSpecItemReportByShopPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convertToOrderSpecItemReportByShopDTO(po))
                .collect(Collectors.toList());
    }

    private OrderSpecItemReportByShopDTO convertToOrderSpecItemReportByShopDTO(OrderSpecItemReportByShopPO po) {
        if (po == null) {
            return null;
        }

        OrderSpecItemReportByShopDTO dto = new OrderSpecItemReportByShopDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setOrderCreatedDay(po.getOrderCreatedDay());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setShopCode(po.getShopCode());
        dto.setSpecItemCode(po.getSpecItemCode());
        dto.setAmount(po.getAmount());
        return dto;
    }

    private List<OrderToppingReportByShopDTO> convertToOrderToppingReportByShopDTO(
            List<OrderToppingReportByShopPO> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return null;
        }

        return poList.stream()
                .map(po -> convertToOrderToppingReportByShopDTO(po))
                .collect(Collectors.toList());
    }

    private OrderToppingReportByShopDTO convertToOrderToppingReportByShopDTO(OrderToppingReportByShopPO po) {
        if (po == null) {
            return null;
        }

        OrderToppingReportByShopDTO dto = new OrderToppingReportByShopDTO();
        dto.setGmtCreated(po.getGmtCreated());
        dto.setGmtModified(po.getGmtModified());
        dto.setOrderCreatedDay(po.getOrderCreatedDay());
        dto.setShopGroupCode(po.getShopGroupCode());
        dto.setShopCode(po.getShopCode());
        dto.setToppingCode(po.getToppingCode());
        dto.setAmount(po.getAmount());
        return dto;
    }
}
