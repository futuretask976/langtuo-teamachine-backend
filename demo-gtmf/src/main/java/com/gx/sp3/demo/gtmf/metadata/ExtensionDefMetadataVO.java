package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;

/**
 * @author miya
 */
@Data
public class ExtensionDefMetadataVO {
    /**
     *
     */
    private String facadeClass;

    /**
     *
     */
    private String extensionCode;

    /**
     *
     */
    private String extensionDesc;

    /**
     *
     */
    private String extensionClass;

    /**
     *
     */
    private String extensionGetterInvoker;

    public static ExtensionDefMetadataVO from(ExtensionDefMetadata data) {
        ExtensionDefMetadataVO vo = new ExtensionDefMetadataVO();
        vo.setFacadeClass(data.getFacadeClass().toString());
        vo.setExtensionCode(data.getExtensionCode());
        vo.setExtensionDesc(data.getExtensionDesc());
        vo.setExtensionClass(data.getExtensionClass().toString());
        vo.setExtensionGetterInvoker(data.getExtensionGetterInvoker().toString());
        return vo;
    }
}
