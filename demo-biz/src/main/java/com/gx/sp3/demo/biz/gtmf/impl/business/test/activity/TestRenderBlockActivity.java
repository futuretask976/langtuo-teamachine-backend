package com.gx.sp3.demo.biz.gtmf.impl.business.test.activity;

import com.gx.sp3.demo.biz.gtmf.impl.business.test.ability.DefaultTestRenderBlockAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.context.RenderBlockContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.LinkBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.RenderDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.model.domain.TextBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.RenderRequestDTO;
import com.gx.sp3.demo.biz.gtmf.impl.business.test.transfer.RequestTransfer;
import com.gx.sp3.demo.gtmf.annotation.Activity;

import javax.annotation.Resource;

@Activity
public class TestRenderBlockActivity {
    @Resource(name="com.gx.biz.gtmf.impl.business.test.transfer.RequestTransfer")
    private RequestTransfer requestTransfer;

    @Resource
    private DefaultTestRenderBlockAbility defaultTestRenderBlockAbility;

    public RenderDO render(RenderRequestDTO renderRequestDTO) {
        RenderBlockContext renderBlockContext = requestTransfer.buildContext(renderRequestDTO);

        TextBlockDO textBlockDO = defaultTestRenderBlockAbility.buildTextBlockDO(renderBlockContext);
        System.out.println("!!! " + textBlockDO.toString());

        LinkBlockDO linkBlockDO = defaultTestRenderBlockAbility.buildLinkBlockDO(renderBlockContext);
        System.out.println("!!! " + linkBlockDO.toString());

        RenderDO renderDO = new RenderDO();
        renderDO.setTextBlockDO(textBlockDO);
        renderDO.setLinkBlockDO(linkBlockDO);
        return renderDO;
    }
}
