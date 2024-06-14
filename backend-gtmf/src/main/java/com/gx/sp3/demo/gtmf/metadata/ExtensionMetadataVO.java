package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;

/**
 * @author miya
 */
@Data
public class ExtensionMetadataVO {
    /**
     *
     */
    private String bizCode;

    /**
     *
     */
    private String extension;

    /**
     *
     */
    private String impl;

    /**
     *
     */
    private String underProxyImpl;

    public static ExtensionMetadataVO from(ExtensionMetadata data) {
        ExtensionMetadataVO vo = new ExtensionMetadataVO();
        vo.setBizCode(data.getBizCode());
        vo.setExtension(data.getExtension().toString());
        vo.setImpl(data.getImpl().getClass().toString());
        vo.setUnderProxyImpl(data.getUnderProxyImpl().getClass().toString());
        return vo;
    }
}
