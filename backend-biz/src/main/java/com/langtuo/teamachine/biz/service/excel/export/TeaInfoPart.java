package com.langtuo.teamachine.biz.service.excel.export;

import lombok.Data;

@Data
public class TeaInfoPart {
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
    private int state;

    /**
     * 茶类型编码
     */
    private String teaTypeCode;

    /**
     * 茶品图片链接
     */
    private String imgLink;
}
