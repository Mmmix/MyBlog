package com.wust.myblog.service;

import com.wust.myblog.modal.Comment;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;

public interface ICommentService {
    Result addComment(Comment comment);

    Result deleteComment(Comment comment);

    Result selectCommentByBlogId(Integer id);

    Result selectCommentByParent(Comment comment);

    Result selectCommentByUserId();
}
