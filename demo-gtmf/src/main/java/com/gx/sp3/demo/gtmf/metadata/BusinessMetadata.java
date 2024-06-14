package com.gx.sp3.demo.gtmf.metadata;

import com.gx.sp3.demo.gtmf.extension.BusinessTemplate;
import com.gx.sp3.demo.gtmf.gray.BusinessGrayStrategy;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author miya
 */
@Data
public class BusinessMetadata {
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
    private List<ExtensionMetadata> extensionMetaData = new ArrayList<>();

    /**
     *
     */
    private BusinessTemplate businessTemplate;

    /**
     *
     */
    private BusinessGrayStrategy businessGrayStrategy;

    @Override
    public String toString() {
        return "BusinessMetadata{" +
                "bizCode='" + bizCode + '\'' +
                ", extensionMetaData=" + extensionMetaData +
                ", businessTemplate=" + businessTemplate.getClass() +
                ", businessGrayStrategy=" + businessGrayStrategy.getClass() +
                '}';
    }
}
