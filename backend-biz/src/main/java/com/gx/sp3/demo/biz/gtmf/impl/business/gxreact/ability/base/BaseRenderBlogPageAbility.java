package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ability.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.base.BlockLike;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.base.PageLike;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

import java.util.List;

/**
 * @author miya
 * @param <T>
 */
public interface BaseRenderBlogPageAbility<T extends List<? extends BlockLike>, R extends PageLike> {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    T buildBlogBlockList(RenderBlogPageContext context) throws GTmfException;

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    R buildBlogPage(RenderBlogPageContext context) throws GTmfException;
}
