package com.langtuo.teamachine.api.request.record;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

@Data
public class SupplyActRecordPutRequest {
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
     * 机器编码
     */
    private String machineCode;

    /**
     * 店铺编码
     */
    private String shopCode;

    /**
     * 店铺组编码
     */
    private String shopGroupCode;

    /**
     * 补充时间
     */
    private Date supplyTime;

    /**
     * 物料名称
     */
    private String toppingCode;

    /**
     * 管道序号
     */
    private Integer pipelineNum;

    /**
     * 补充数量
     */
    private Integer supplyAmount;

    /**
     *
     * @return
     */
    public boolean isValid() {
        if (StringUtils.isBlank(tenantCode)
                || StringUtils.isBlank(idempotentMark)
                || StringUtils.isBlank(machineCode)
                || StringUtils.isBlank(shopCode)
                || StringUtils.isBlank(shopGroupCode)
                || supplyTime == null
                || StringUtils.isBlank(toppingCode)
                || pipelineNum <= 0) {
            return false;
        }
        return true;
    }
}
