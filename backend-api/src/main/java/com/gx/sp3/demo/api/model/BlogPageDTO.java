package com.gx.sp3.demo.api.model;

import java.util.List;

public class BlogPageDTO {
    /**
     *
     */
    private String title;

    /**
     *
     */
    private String desc;

    /**
     *
     */
    private List<BlogBlockDTO> blogBlockDTOList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<BlogBlockDTO> getBlogBlockDTOList() {
        return blogBlockDTOList;
    }

    public void setBlogBlockDTOList(List<BlogBlockDTO> blogBlockDTOList) {
        this.blogBlockDTOList = blogBlockDTOList;
    }
}
