package com.wust.myblog.controller;

import com.wust.myblog.modal.Comment;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    ICommentService iCommentService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Result add(Comment comment){
        return iCommentService.addComment(comment);
    }

    @RequestMapping(value = "delete",method = RequestMethod.GET)
    public Result delete(Comment comment){
        return iCommentService.deleteComment(comment);
    }

    @RequestMapping(value = "selectByBlogId",method = RequestMethod.GET)
    public Result selectByBlogId(Integer id,Integer pageNum,Integer pageSize){
        return iCommentService.selectCommentByBlogId(id,pageNum,pageSize);
    }

    @RequestMapping(value = "selectByParentId",method = RequestMethod.GET)
    public Result selectByParentId(Comment comment){
        return iCommentService.selectCommentByParent(comment);
    }
}
