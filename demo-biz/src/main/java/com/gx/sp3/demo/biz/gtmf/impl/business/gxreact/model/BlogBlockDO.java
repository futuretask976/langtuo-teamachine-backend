package com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model;

import com.gx.sp3.demo.biz.gtmf.impl.business.gxreact.model.base.BlockLike;
import lombok.Data;

import java.util.Date;

@Data
public class BlogBlockDO implements BlockLike {
    /**
     *
     */
    private long id;

    /**
     *
     */
    private String imageUrl;

    /**
     *
     */
    private Date publishDate;

    /**
     *
     */
    private String author;

    /**
     *
     */
    private long commentCnt;

    /**
     *
     */
    private String title;

    @Override
    public String getBizCode() {
        return null;
    }

    @Override
    public void setBizCode(String bizCode) {

    }

    public static BlogBlockDO of(long id, String imageUrl, Date publishDate, String author, Long commentCnt, String title) {
        BlogBlockDO blogBlockDO = new BlogBlockDO();
        blogBlockDO.setId(id);
        blogBlockDO.setImageUrl(imageUrl);
        blogBlockDO.setPublishDate(publishDate);
        blogBlockDO.setAuthor(author);
        blogBlockDO.setCommentCnt(commentCnt);
        blogBlockDO.setTitle(title);
        return blogBlockDO;
    }

    @Override
    public String toString() {
        return "BlogBlockDO["
                + "id=" + id
                + "imageUrl=" + imageUrl
                + "publishDate=" + publishDate
                + "author=" + author
                + "commentCnt=" + commentCnt
                + "title=" + title + "]";
    }
}
