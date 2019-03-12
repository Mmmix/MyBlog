package com.wust.myblog.controller;

import cn.hutool.core.util.StrUtil;
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
    public Result detail(Integer id){

        return iBlogService.selectBlogById(id);
    }

    @RequestMapping(value = "list/{pageNum}",method = RequestMethod.GET)
    public Result list(String title, @PathVariable Integer pageNum, Integer pageSize,String category){
        return iBlogService.listBlog(title,pageNum,pageSize,category);
    }

    @RequestMapping(value = "listByCategory/{pageNum}", method = RequestMethod.GET)
    public Result listByCategory(String category, @PathVariable Integer pageNum, Integer pageSize){
        return iBlogService.listBlogByCategory(category, pageNum, pageSize);
    }
}
