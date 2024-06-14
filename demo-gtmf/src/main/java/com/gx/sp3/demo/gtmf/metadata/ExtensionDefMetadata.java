package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author miya
 */
@Data
public class ExtensionDefMetadata {
    /**
     *
     */
    private Class<?> facadeClass;

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
    private Class<?> extensionClass;

    /**
     *
     */
    private Method extensionGetterInvoker;

    /**
     *
     * @param facadeClass
     * @param extensionsClass
     * @param getterInvoker
     * @param extensionCode
     * @param extensionDesc
     * @return
     */
    public static ExtensionDefMetadata of(Class<?> facadeClass,
                                          Class<?> extensionsClass,
                                          Method getterInvoker,
                                          String extensionCode,
                                          String extensionDesc) {
        ExtensionDefMetadata extensionDefMetadata = new ExtensionDefMetadata();
        extensionDefMetadata.setFacadeClass(facadeClass);
        extensionDefMetadata.setExtensionClass(extensionsClass);
        extensionDefMetadata.setExtensionCode(extensionCode);
        extensionDefMetadata.setExtensionDesc(extensionDesc);
        extensionDefMetadata.setExtensionGetterInvoker(getterInvoker);
        return extensionDefMetadata;
    }

    /**
     *
     * @param businessMetadata
     * @param impl
     * @param underProxyImpl
     * @return
     */
    public ExtensionMetadata toRuntimeExtensionMetadata(BusinessMetadata businessMetadata, Object impl, Object underProxyImpl) {
        ExtensionMetadata extensionMetadata = new ExtensionMetadata();
        extensionMetadata.setExtension(extensionClass);
        extensionMetadata.setImpl(impl);
        extensionMetadata.setBusinessMetadata(businessMetadata);
        extensionMetadata.setBizCode(businessMetadata.getBizCode());
        extensionMetadata.setUnderProxyImpl(underProxyImpl);
        return extensionMetadata;
    }

    @Override
    public String toString() {
        return "ExtensionDefMetadata{" +
                "facadeClass=" + facadeClass +
                ", extensionCode='" + extensionCode + '\'' +
                ", extensionDesc='" + extensionDesc + '\'' +
                ", extensionClass=" + extensionClass +
                ", extensionGetterInvoker=" + extensionGetterInvoker +
                '}';
    }
}
