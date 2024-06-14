package com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.gtmf.annotation.BlockReference;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 */
public interface BaseRenderBlockLinkExtPoints {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "linkStyle")
    default String getLinkStyle(RenderBlockContext context) throws GTmfException {
        return null;
    }

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    @BlockReference(blockName = "testBlock", field = "linkColor")
    default String getLinkColor(RenderBlockContext context) throws GTmfException {
        return null;
    }
}
