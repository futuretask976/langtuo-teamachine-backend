package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BusinessMetadataVO {
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
     */
    private List<ExtensionMetadataVO> extensionMetaData = new ArrayList<>();

    /**
     *
     */
    private String businessTemplate;

    /**
     *
     * @param data
     * @return
     */
    public static BusinessMetadataVO from(BusinessMetadata data) {
        BusinessMetadataVO vo = new BusinessMetadataVO();
        vo.setBizCode(data.getBizCode());
        vo.setDesc(data.getDesc());
        vo.setBusinessTemplate(data.getBusinessTemplate().getClass().toString());
        data.getExtensionMetaData().stream()
                .map(ExtensionMetadataVO::from)
                .forEach(vo.extensionMetaData::add);
        return vo;
    }
}
