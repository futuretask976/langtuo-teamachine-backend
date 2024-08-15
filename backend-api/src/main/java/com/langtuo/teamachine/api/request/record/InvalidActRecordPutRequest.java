package com.langtuo.teamachine.api.request.record;

import com.langtuo.teamachine.api.utils.RegexUtils;
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
        if (RegexUtils.isValidStr(tenantCode, true)
                && RegexUtils.isValidStr(idempotentMark, true)
                && RegexUtils.isValidStr(machineCode, true)
                && RegexUtils.isValidStr(shopCode, true)
                && RegexUtils.isValidStr(shopGroupCode, true)
                && RegexUtils.isValidStr(toppingCode, true)
                && invalidTime != null
                && pipelineNum > 0) {
            return true;
        }
        return false;
    }
}
