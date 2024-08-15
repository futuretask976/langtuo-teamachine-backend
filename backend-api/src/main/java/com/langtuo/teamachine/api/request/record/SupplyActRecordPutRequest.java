package com.langtuo.teamachine.api.request.record;

import com.langtuo.teamachine.api.utils.RegexUtils;
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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(idempotentMark, true)
                && RegexUtils.isValidStr(machineCode, true)
                && RegexUtils.isValidStr(shopCode, true)
                && RegexUtils.isValidStr(shopGroupCode, true)
                && RegexUtils.isValidStr(toppingCode, true)
                && supplyTime != null
                && pipelineNum > 0) {
            return true;
        }
        return false;
    }
}
