package com.gx.sp3.demo.api.service;

import com.gx.sp3.demo.api.model.BlogBlockDTO;
import com.gx.sp3.demo.api.model.BlogPageDTO;
import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.api.result.GxResult;

import java.util.List;

public interface RenderBlogPageService {
    /**
     *
     * @return
     */
    GxResult<List<BlogBlockDTO>> fetchBlogList(RenderBlogPageRequest renderBlogPageRequest);

    /**
     *
     * @return
     */
    GxResult<BlogPageDTO> fetchBlogPage(RenderBlogPageRequest renderBlogPageRequest);
}
