package com.gx.sp3.demo.gtmf.metadata;

import com.gx.sp3.demo.gtmf.extension.BusinessTemplate;
import com.gx.sp3.demo.gtmf.gray.BusinessGrayStrategy;
import lombok.Data;

/**
 * @author miya
 */
@Data
public class BusinessDefMetadata {
    /**
     *
     */
    private Class<? extends BusinessTemplate> templateClass;

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
    private BusinessGrayStrategy businessGrayStrategy;

    /**
     *
     * @param templateClass
     * @param bizCode
     * @param desc
     * @return
     */
    public static BusinessDefMetadata of(Class<? extends BusinessTemplate> templateClass, String bizCode, String desc) {
        BusinessDefMetadata businessDefMetadata = new BusinessDefMetadata();
        businessDefMetadata.setTemplateClass(templateClass);
        businessDefMetadata.setBizCode(bizCode);
        businessDefMetadata.setDesc(desc);
        return businessDefMetadata;
    }

    /**
     *
     * @param businessTemplate
     * @return
     */
    public BusinessMetadata toRuntimeBusinessMetadata(BusinessTemplate businessTemplate) {
        BusinessMetadata businessMetadata = new BusinessMetadata();
        businessMetadata.setBizCode(bizCode);
        businessMetadata.setDesc(desc);
        businessMetadata.setBusinessTemplate(businessTemplate);
        businessMetadata.setBusinessGrayStrategy(this.getBusinessGrayStrategy());
        return businessMetadata;
    }

    @Override
    public String toString() {
        return "BusinessDefMetadata{" +
                "templateClass=" + templateClass +
                ", bizCode='" + bizCode + '\'' +
                ", businessGrayStrategy=" + businessGrayStrategy +
                '}';
    }
}
