package com.gx.sp3.demo.gtmf.metadata;

import lombok.Data;


@Data
public class ExtensionMetadata {
    /**
     *
     */
    private String bizCode;

    /**
     *
     */
    private Class<?> extension;

    /**
     *
     */
    private Object impl;

    /**
     *
     */
    private Object underProxyImpl;

    /**
     *
     */
    private BusinessMetadata businessMetadata;

    @Override
    public String toString() {
        return "ExtensionMetadata{" +
                "bizCode='" + bizCode + '\'' +
                ", extension=" + extension +
                ", impl=" + impl.getClass() +
                ", businessMetadata=" + businessMetadata.getBusinessTemplate().getClass() +
                '}';
    }
}
