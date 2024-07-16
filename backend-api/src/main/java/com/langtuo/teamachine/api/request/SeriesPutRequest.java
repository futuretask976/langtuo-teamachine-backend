package com.langtuo.teamachine.api.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SeriesPutRequest {
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
     * 系列编码
     */
    private String seriesCode;

    /**
     * 系列名称
     */
    private String seriesName;

    /**
     * 图片链接
     */
    private String imgLink;

    /**
     * 系列-茶品关系
     */
    private List<SeriesTeaRelPutRequest> seriesTeaRelList;
}
