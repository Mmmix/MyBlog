package com.wust.myblog.service;

import com.wust.myblog.modal.Comment;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;

public interface ICommentService {
    Result addComment(Comment comment);

    Result deleteComment(Comment comment);

    Result selectCommentByBlogId(Integer id,Integer pageNum,Integer pageSize);

    Result selectCommentByParent(Comment comment);

}
