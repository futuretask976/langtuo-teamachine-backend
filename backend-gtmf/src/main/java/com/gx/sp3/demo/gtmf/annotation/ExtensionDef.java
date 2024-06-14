package com.gx.sp3.demo.gtmf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author miya
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtensionDef {
    /**
     *
     * @return
     */
    String code() default "";

    /**
     *
     * @return
     */
    String desc() ;
}
