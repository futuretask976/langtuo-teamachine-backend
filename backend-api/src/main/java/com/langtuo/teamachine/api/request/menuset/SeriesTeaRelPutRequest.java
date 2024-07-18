package com.langtuo.teamachine.api.request.menuset;

import lombok.Data;

@Data
public class SeriesTeaRelPutRequest {
    /**
     * 系列编码
     */
    private String seriesCode;

    /**
     * 茶品编码
     */
    private String teaCode;
}
