package com.wust.myblog.service;

import com.wust.myblog.modal.Blog;
import com.wust.myblog.modal.Result;

public interface IBlogService {
    Result addBlog(Blog blog, String tags);
    Result modBlog(Blog blog, String tags);
    Result listBlog(String title,Integer pageNum,Integer pageSize);
    Result selectBlogById(Integer id);
    Result deleteBlog(Integer id);
    Result setBlogTag(Blog blog);
    Result listBlogByCategory(String category, Integer pageNum, Integer pageSize);
}
