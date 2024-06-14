package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.base.PageLike;
import lombok.Data;

import java.util.List;

@Data
public class BlogPageDO implements PageLike {
    /**
     *
     */
    private List<BlogBlockDO> blogBlockDOList;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String desc;

    @Override
    public String getBizCode() {
        return null;
    }

    @Override
    public void setBizCode(String bizCode) {

    }

    @Override
    public String toString() {
        return "LinkBlockDO["
                + "title=" + title
                + ", desc=" + desc + "]";
    }
}
