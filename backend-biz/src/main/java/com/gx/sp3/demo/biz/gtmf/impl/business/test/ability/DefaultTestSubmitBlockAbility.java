package com.gx.sp3.demo.biz.gtmf.impl.business.test.ability;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.customized.TestSubmitBlockAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.SubmitBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.TestExecuteExtRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockInputExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessSubmitBlockSelectExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.InputBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.SelectBlockDO;
import com.gx.sp3.demo.gtmf.annotation.AbilityImpl;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import com.gx.sp3.demo.gtmf.extension.SystemAbility;

@AbilityImpl(desc = "默认Test提交实现")
public class DefaultTestSubmitBlockAbility extends SystemAbility implements TestSubmitBlockAbility {
    @Override
    public InputBlockDO buildInputBlockDO(SubmitBlockContext context) throws GTmfException {
        InputBlockDO inputBlockDO = new InputBlockDO();

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessSubmitBlockInputExtPoints.class,
                ext->ext.getInputStyle(context))
                .ifPresent(rst-> inputBlockDO.setInputStyle(rst.getResult()));

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessSubmitBlockInputExtPoints.class,
                ext->ext.getInputVal(context))
                .ifPresent(rst-> inputBlockDO.setInputVal(rst.getResult()));

        return inputBlockDO;
    }

    @Override
    public SelectBlockDO buildSelectBlockDO(SubmitBlockContext context) throws GTmfException {
        SelectBlockDO selectBlockDO = new SelectBlockDO();

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessSubmitBlockSelectExtPoints.class,
                ext->ext.getSelectStyle(context))
                .ifPresent(rst-> selectBlockDO.setSelectStyle(rst.getResult()));

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessSubmitBlockSelectExtPoints.class,
                ext->ext.getSelectVal(context))
                .ifPresent(rst-> selectBlockDO.setSelectVal(rst.getResult()));

        return selectBlockDO;
    }
}
