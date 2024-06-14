package com.gx.sp3.demo.gtmf.scan;

import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * 遍历到类时的操作
 *
 * copied form ng lazy
 * @author miya
 */
public interface ScanHandler {
    /**
     *
     * @param resource
     * @param metadataReader
     * @param metadataReaderFactory
     * @throws Exception
     */
    void handle(Resource resource, MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws Exception;
}
