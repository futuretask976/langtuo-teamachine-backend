package com.gx.sp3.demo.biz.service.impl;

import com.gx.sp3.demo.api.model.BlogBlockDTO;
import com.gx.sp3.demo.api.model.BlogPageDTO;
import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.RenderBlogPageService;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.activity.RenderBlogPageActivity;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogBlockDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.BlogPageDO;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.transfer.RequestTransfer;
import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.transfer.ResultTransfer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RenderBlogPageSerivceImpl implements RenderBlogPageService {
    @Resource
    private RenderBlogPageActivity renderBlogPageActivity;

    @Resource
    private RequestTransfer requestTransfer;

    @Resource
    private ResultTransfer resultTransfer;

    @Override
    public GxResult<List<BlogBlockDTO>> fetchBlogList(RenderBlogPageRequest request) {
        List<BlogBlockDO> list = renderBlogPageActivity.renderBlogBlockList(request);
        if (!CollectionUtils.isEmpty(list)) {
            return GxResult.success(resultTransfer.transferToBlogBlockDTO(list));
        } else {
            return GxResult.error("failed to render blog page");
        }
    }

    @Override
    public GxResult<BlogPageDTO> fetchBlogPage(RenderBlogPageRequest request) {
        BlogPageDO blogPage = renderBlogPageActivity.renderBlogPage(request);
        if (blogPage != null) {
            return GxResult.success(resultTransfer.transferToBlogPageDTO(blogPage));
        } else {
            return GxResult.error("failed to render blog page");
        }
    }
}
