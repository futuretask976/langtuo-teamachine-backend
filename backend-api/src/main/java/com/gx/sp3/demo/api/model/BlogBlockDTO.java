package com.gx.sp3.demo.api.model;

import java.util.Date;

public class BlogBlockDTO {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(long commentCnt) {
        this.commentCnt = commentCnt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
