package com.gx.sp3.demo.gtmf.annotation;

/**
 * @author miya
 */
public @interface BlockReference {
    /**
     *
     * @return
     */
    String blockName();

    /**
     *
     * @return
     */
    String field() default "";
}
