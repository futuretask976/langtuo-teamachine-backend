package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ability;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ability.customized.RenderBlogPageAbility;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.context.RenderBlogPageContext;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.GxExecuteExtRequest;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.ext.customized.GxBusinessRenderBlogPageExtPoints;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import com.gx.sp3.demo.gtmf.annotation.AbilityImpl;
import com.gx.sp3.demo.gtmf.exception.GTmfException;
import com.gx.sp3.demo.gtmf.extension.ExecuteExtResult;
import com.gx.sp3.demo.gtmf.extension.SystemAbility;

import java.util.List;

@AbilityImpl(desc = "默认渲染Blog页面实现")
public class DefaultRenderBlogPageAbility extends SystemAbility implements RenderBlogPageAbility {
    @Override
    public List<BlogBlockDO> buildBlogBlockList(RenderBlogPageContext context) throws GTmfException {
        GxExecuteExtRequest gxExecuteExtRequest = GxExecuteExtRequest.from(context);
        ExecuteExtResult<List<BlogBlockDO>> executeExtResult = executeUntilNonNull(gxExecuteExtRequest, GxBusinessRenderBlogPageExtPoints.class, ext->ext.fetchBlogBlockList(context)).get();

        List<BlogBlockDO> list = executeExtResult == null ? null : executeExtResult.getResult();
        return list;
    }

    @Override
    public BlogPageDO buildBlogPage(RenderBlogPageContext context) throws GTmfException {
        ExecuteExtResult<BlogPageDO> executeExtResult = executeUntilNonNull(
                GxExecuteExtRequest.from(context), GxBusinessRenderBlogPageExtPoints.class,
                ext->ext.fetchBlogPage(context)).get();

        BlogPageDO blogPageDO = executeExtResult == null ? null : executeExtResult.getResult();

        return blogPageDO;
    }
}
