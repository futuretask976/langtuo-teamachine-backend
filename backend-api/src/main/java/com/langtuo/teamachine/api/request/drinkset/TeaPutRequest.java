package com.langtuo.teamachine.api.request.drinkset;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TeaPutRequest {
    /**
     * 茶编码
     */
    private String teaCode;

    /**
     * 茶名称
     */
    private String teaName;

    /**
     * 外部茶编码
     */
    private String outerTeaCode;

    /**
     * 状态，0：禁用，1：启用
     */
    private Integer state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

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
     * 茶-物料关系列表
     */
    private List<TeaUnitPutRequest> teaUnitList;
}
