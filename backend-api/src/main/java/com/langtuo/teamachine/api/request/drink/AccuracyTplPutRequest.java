package com.langtuo.teamachine.api.request.drink;

import com.langtuo.teamachine.api.utils.RegexUtils;
import lombok.Data;

import java.util.Map;

@Data
public class AccuracyTplPutRequest {
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 溢出允许单位，0：固定值，1：百分比
     */
    private int overUnit;

    /**
     * 溢出数值
     */
    private int overAmount;

    /**
     * 不及允许单位，0：固定值，1：百分比
     */
    private int underUnit;

    /**
     * 不及数值
     */
    private int underAmount;

    /**
     * 应用物料编码
     */
    private String toppingCode;

    /**
     * 备注
     */
    private String comment;

    /**
     * 参数校验
     * @return
     */
    public boolean isValid() {
        if (RegexUtils.isValidCode(tenantCode, true)
                // && RegexUtils.isValidCode(comment, false)
                && RegexUtils.isValidCode(templateCode, true)
                && RegexUtils.isValidName(templateName, true)
                && RegexUtils.isValidCode(toppingCode, true)) {
            return true;
        }
        return false;
    }
}
