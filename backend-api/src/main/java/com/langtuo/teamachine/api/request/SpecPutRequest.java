package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SpecPutRequest {
    /**
     * 规格编码
     */
    private String specCode;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 备注
     */
    private String comment;

    /**
     * 额外信息，格式：a:b;c:d
     */
    private Map<String, String> extraInfo;

    /**
     *
     */
    private List<SpecItemPutRequest> specItemList;
}
