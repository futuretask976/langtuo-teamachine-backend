package com.gx.sp3.demo.web.controller;

import com.gx.sp3.demo.api.model.BlogPageDTO;
import com.gx.sp3.demo.api.request.RenderBlogPageRequest;
import com.gx.sp3.demo.api.result.GxResult;
import com.gx.sp3.demo.api.service.RenderBlogPageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gtmf")
public class GtmfRestfulController {
    @Resource
    private RenderBlogPageService renderBlogPageService;

    /**
     * Access this method by http://localhost:8080/gxsp3demo/gtmf/blog
     * @return
     */
    @GetMapping(value = "/blog")
    public GxResult<BlogPageDTO> page() {
        GxResult<BlogPageDTO> result = renderBlogPageService.fetchBlogPage(new RenderBlogPageRequest());
        return result;
    }
}
