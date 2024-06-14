package com.gx.sp3.demo.biz.gtmf.impl.business.test.ability;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.customized.TestRenderBlockAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.TestExecuteExtRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockLinkExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.ext.customized.TestBusinessRenderBlockTextExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.LinkBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.TextBlockDO;
import com.gx.sp3.demo.gtmf.annotation.AbilityImpl;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import com.gx.sp3.demo.gtmf.extension.SystemAbility;

@AbilityImpl(desc = "默认Test渲染实现")
public class DefaultTestRenderBlockAbility extends SystemAbility implements TestRenderBlockAbility {
    @Override
    public TextBlockDO buildTextBlockDO(RenderBlockContext context) throws GTmfException {
        TextBlockDO textBlockDO = new TextBlockDO();

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessRenderBlockTextExtPoints.class,
                ext->ext.getTextStyle(context))
                .ifPresent(rst-> textBlockDO.setTextStyle(rst.getResult()));

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessRenderBlockTextExtPoints.class,
                ext->ext.getTextColor(context))
                .ifPresent(rst-> textBlockDO.setTextColor(rst.getResult()));

        return textBlockDO;
    }

    @Override
    public LinkBlockDO buildLinkBlockDO(RenderBlockContext context) throws GTmfException {
        LinkBlockDO linkBlockDO = new LinkBlockDO();

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessRenderBlockLinkExtPoints.class,
                ext->ext.getLinkStyle(context))
                .ifPresent(rst-> linkBlockDO.setLinkStyle(rst.getResult()));

        executeUntilNonNull(
                TestExecuteExtRequest.from(context), TestBusinessRenderBlockLinkExtPoints.class,
                ext->ext.getLinkColor(context))
                .ifPresent(rst-> linkBlockDO.setLinkColor(rst.getResult()));

        return linkBlockDO;
    }
}
