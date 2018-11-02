package com.wust.myblog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wust.myblog.mapper.BlogMapper;
import com.wust.myblog.modal.Blog;
import com.wust.myblog.modal.BlogExample;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.IBlogService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService implements IBlogService {
    @Autowired
    BlogMapper blogMapper;

    @Override
    public Result addBlog(Blog blog) {
        if (blog!=null){
            blog.setComment(0);
            blog.setRead(0);
            if (blogMapper.insertSelective(blog)>0)
                return ResultUtil.success("新增成功");
        }
        return ResultUtil.error("新增失败");
    }

    @Override
    public Result listBlog(String title,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize);
        PageInfo<Blog> pageInfo;
        if (StrUtil.isBlank(title)){
            pageInfo = new PageInfo<>(blogMapper.selectByExample(null));
        }else {
            BlogExample blogExample = new BlogExample();
            BlogExample.Criteria criteria = blogExample.createCriteria();
            criteria.andTitleLike("%"+title+"%");
            pageInfo = new PageInfo<>(blogMapper.selectByExample(blogExample));
        }
        return ResultUtil.success(pageInfo);
    }

    @Override
    public Result selectBlogById(Integer id) {
        return ResultUtil.success(blogMapper.selectByPrimaryKey(id));
    }

    @Override
    public Result deleteBlog(Blog blog) {
        return ResultUtil.success(blogMapper.deleteByPrimaryKey(blog.getId()));
    }

    @Override
    public Result setBlogTag(Blog blog) {
        return ResultUtil.success(blogMapper.updateByPrimaryKeySelective(blog));
    }
}
