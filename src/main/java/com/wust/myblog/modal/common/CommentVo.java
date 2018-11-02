package com.wust.myblog.modal.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wust.myblog.modal.Comment;

import java.util.Date;
import java.util.List;

public class CommentVo {

    private Integer id;

    private Integer blogId;

    private Integer userId;

    private String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private List<Comment> childComment;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getChildComment() {
        return childComment;
    }

    public void setChildComment(List<Comment> childComment) {
        this.childComment = childComment;
    }
}
