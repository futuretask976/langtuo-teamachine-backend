package com.gx.sp3.demo.gtmf.annotation;


import java.lang.annotation.*;

/**
 * @author miya
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AbilityImpl {
    String desc();
}
