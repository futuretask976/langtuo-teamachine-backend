package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

import java.util.List;

/**
 * @author miya
 */
public interface BaseRenderBlogPageExtPoints {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    default List<BlogBlockDO> fetchBlogBlockList(RenderBlogPageContext context) throws GTmfException {
        System.out.println("BaseRenderBlogPageExtPoints#fetchBlogBlockList entering");
        return null;
    }

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    default BlogPageDO fetchBlogPage(RenderBlogPageContext context) throws GTmfException {
        System.out.println("!!! BaseRenderBlogPageExtPoints#fetchBlogPage entering");
        return null;
    }
}
