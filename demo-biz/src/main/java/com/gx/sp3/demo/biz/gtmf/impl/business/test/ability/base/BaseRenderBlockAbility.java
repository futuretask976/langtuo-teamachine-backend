package com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base.BlockLike;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 * @param <T>
 */
public interface BaseRenderBlockAbility<T extends BlockLike, R extends BlockLike> {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    T buildTextBlockDO(RenderBlockContext context) throws GTmfException;

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    R buildLinkBlockDO(RenderBlockContext context) throws GTmfException;
}
