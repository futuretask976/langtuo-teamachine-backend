package com.langtuo.teamachine.api.request.record;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

@Data
public class InvalidActRecordPutRequest {
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
     * 失效时间
     */
    private Date invalidTime;

    /**
     * 物料名称
     */
    private String toppingCode;

    /**
     * 管道序号
     */
    private int pipelineNum;

    /**
     * 失效数量
     */
    private int invalidAmount;

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
                || invalidTime == null
                || StringUtils.isBlank(toppingCode)
                || pipelineNum <= 0) {
            return false;
        }
        return true;
    }
}
