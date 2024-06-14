package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.gtmf.annotation.BlockReference;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 */
public interface BaseRenderBlockTextExtPoints {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "textStyle")
    default String getTextStyle(RenderBlockContext context) throws GTmfException {
        return null;
    }

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "textColor")
    default String getTextColor(RenderBlockContext context) throws GTmfException {
        return null;
    }
}
