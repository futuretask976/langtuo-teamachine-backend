package com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.base;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.base.BlockLike;
import com.gx.sp3.demo.gtmf.exception.GTmfException;

/**
 * @author miya
 * @param <T>
 */
public interface BaseSubmitBlockAbility<T extends BlockLike, R extends BlockLike> {
    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    T buildInputBlockDO(SubmitBlockContext context) throws GTmfException;

    /**
     *
     * @param context
     * @return
     * @throws GTmfException
     */
    R buildSelectBlockDO(SubmitBlockContext context) throws GTmfException;
}
