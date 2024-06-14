package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.activity;

import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ability.DefaultRenderBlogPageAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.transfer.RequestTransfer;
import com.gx.sp3.demo.gtmf.annotation.Activity;

import javax.annotation.Resource;
import java.util.List;

@Activity
public class RenderBlogPageActivity {
    @Resource(name="com.gx.biz.gtmf.impl.business.gxreact.transfer.RequestTransfer")
    private RequestTransfer requestTransfer;

    @Resource
    private DefaultRenderBlogPageAbility defaultRenderBlogPageAbility;

    public List<BlogBlockDO> renderBlogBlockList(RenderBlogPageRequest renderRequest) {
        RenderBlogPageContext renderBlockContext = requestTransfer.transferToRenderBlogPageContext(renderRequest);

        List<BlogBlockDO> blogBlockDOList = defaultRenderBlogPageAbility.buildBlogBlockList(renderBlockContext);

        return blogBlockDOList;
    }

    public BlogPageDO renderBlogPage(RenderBlogPageRequest renderRequest) {
        RenderBlogPageContext renderBlockContext = requestTransfer.transferToRenderBlogPageContext(renderRequest);

        List<BlogBlockDO> blogBlockDOList = defaultRenderBlogPageAbility.buildBlogBlockList(renderBlockContext);

        BlogPageDO blogPageDO = defaultRenderBlogPageAbility.buildBlogPage(renderBlockContext);
        blogPageDO.setBlogBlockDOList(blogBlockDOList);

        return blogPageDO;
    }
}
