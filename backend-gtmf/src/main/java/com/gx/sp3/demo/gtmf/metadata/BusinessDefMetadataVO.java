package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;

@Data
public class BusinessDefMetadataVO {
    /**
     *
     */
    private String templateClass;

    /**
     *
     */
    private String bizCode;

    /**
     *
     */
    private String desc;

    /**
     *
     * @param data
     * @return
     */
    public static BusinessDefMetadataVO from(BusinessDefMetadata data) {
        BusinessDefMetadataVO vo = new BusinessDefMetadataVO();
        vo.setDesc(data.getDesc());
        vo.setBizCode(data.getBizCode());
        vo.setTemplateClass(data.getTemplateClass().toString());
        return vo;
    }
}
