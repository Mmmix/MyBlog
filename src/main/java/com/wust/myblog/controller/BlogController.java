package com.wust.myblog.controller;

import com.github.pagehelper.PageInfo;
import com.wust.myblog.modal.Blog;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.IBlogService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.ReentrantLock;

@CrossOrigin
@RestController
@RequestMapping("blog")
public class BlogController {
    @Autowired
    private IBlogService iBlogService;

    @RequestMapping(value = "detail",method = RequestMethod.GET)
    public String detail(Integer id){
        Result result = iBlogService.selectBlogById(id);

        return ((Blog)result.getData()).getContext();
    }

    @RequestMapping(value = "list/{pageNum}")
    public Result list(String title, @PathVariable Integer pageNum, Integer pageSize){
        return iBlogService.listBlog(title,pageNum,pageSize);
    }

}
