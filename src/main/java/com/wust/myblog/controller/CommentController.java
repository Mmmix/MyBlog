package com.wust.myblog.controller;

import com.wust.myblog.modal.Comment;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    ICommentService iCommentService;

    @RequestMapping("add")
    public Result add(Comment comment){
        return iCommentService.addComment(comment);
    }

    @RequestMapping("delete")
    public Result delete(Comment comment){
        return iCommentService.deleteComment(comment);
    }

    @RequestMapping("selectByBlogId")
    public Result selectByBlogId(Integer id){
        return iCommentService.selectCommentByBlogId(id);
    }

    @RequestMapping("selectByParentId")
    public Result selectByParentId(Comment comment){
        return iCommentService.selectCommentByParent(comment);
    }

    @RequestMapping("selectByUserId")
    public Result selectByUserId(){
        return iCommentService.selectCommentByUserId();
    }
}
