package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.gtmf.annotation.BlockReference;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 */
public interface BaseSubmitBlockSelectExtPoints {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "selectStyle")
    default String getSelectStyle(SubmitBlockContext context) throws GTmfException {
        return null;
    }

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "selectVal")
    default String getSelectVal(SubmitBlockContext context) throws GTmfException {
        return null;
    }
}
