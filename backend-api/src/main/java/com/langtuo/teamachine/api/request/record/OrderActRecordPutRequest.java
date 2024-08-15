package com.langtuo.teamachine.api.request.record;

import com.langtuo.teamachine.api.utils.CollectionUtils;
import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OrderActRecordPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息
     */
    private Map<String,String> extraInfo;

    /**
     * 幂等标记
     */
    private String idempotentMark;

    /**
     * 设备编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 店铺组名称
     */
    private String shopGroupName;

    /**
     * 订单生成时间
     */
    private Date orderGmtCreated;

    /**
     * 外部订单id
     */
    private String outerOrderId;

    /**
     * 状态，0：未制作，1：制作中，2：已制作，3：有异常，4：已取消
     */
    private int state;

    /**
     * 规格项列表
     */
    private List<OrderSpecItemActRecordPutRequest> specItemList;

    /**
     * 物料列表
     */
    private List<OrderToppingActRecordPutRequest> toppingList;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(idempotentMark, true)
                && RegexUtils.isValidStr(machineCode, true)
                && RegexUtils.isValidStr(shopCode, true)
                && RegexUtils.isValidStr(shopName, true)
                && RegexUtils.isValidStr(shopGroupCode, true)
                && RegexUtils.isValidStr(shopGroupName, true)
                && RegexUtils.isValidStr(outerOrderId, true)
                && isValidSpecItemList()
                && isValidToppingList()) {
            return true;
        }
        return false;
    }

    private boolean isValidSpecItemList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(specItemList)) {
            isValid = false;
        } else {
            for (OrderSpecItemActRecordPutRequest m : specItemList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    private boolean isValidToppingList() {
        boolean isValid = true;
        if (CollectionUtils.isEmpty(toppingList)) {
            isValid = false;
        } else {
            for (OrderToppingActRecordPutRequest m : toppingList) {
                if (!m.isValid()) {
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }
}
