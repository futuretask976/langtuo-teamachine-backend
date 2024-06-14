package com.gx.sp3.demo.gtmf.scan;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 在classpath中遍历所有的类
 * @author miya
 */
public class ScanClassUtils {
    /**
     * 在classpath中遍历在某个包下面所有的类
     *
     * @param basePackage
     *            包名
     * @param handler
     *            遍历到每个类时采取的操作
     */
    public static void scan(String basePackage, ScanHandler handler) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                + "/**/*.class";

        PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(scanner);

        try {
            System.out.println("ScanClassUtils@scan：getResources -> " + scanner.getResources(packageSearchPath).length);
            for (Resource resource : scanner.getResources(packageSearchPath)) {
                if (resource.isReadable()) {
                    handler.handle(resource, metadataReaderFactory.getMetadataReader(resource), metadataReaderFactory);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
