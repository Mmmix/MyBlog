package com.wust.myblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wust.myblog.mapper.BlogMapper;
import com.wust.myblog.modal.Blog;
import com.wust.myblog.modal.BlogExample;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.IBlogService;
import com.wust.myblog.service.ITagService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlogService implements IBlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private ITagService iTagService;

    @Override
    public Result addBlog(Blog blog, String tags) {
        if (blog!=null){
            blog.setComment(0);
            blog.setRead(0);
            Set<String> images = getImgStr(blog.getContext());
            if (images.size()>0)
                blog.setSubimage(images.stream().findFirst().get().replace("amp;",""));
            if (blogMapper.insertSelective(blog)>0) {
                if((!tags.equals("")) && iTagService.addTags(tags, blog.getId())) {
                    return ResultUtil.success("新增成功");
                }
            }
        }
        return ResultUtil.error("新增失败");
    }

    @Override
    public Result listBlog(String title,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize);
        PageInfo<Blog> pageInfo;

        List<Blog> blogList;
        if (StrUtil.isBlank(title)){
            blogList = blogMapper.selectByExampleWithBLOBs(null);
        }else {
            BlogExample blogExample = new BlogExample();
            BlogExample.Criteria criteria = blogExample.createCriteria();
            criteria.andTitleLike("%"+title+"%");
            blogList = blogMapper.selectByExampleWithBLOBs(blogExample);
        }
        blogList.forEach(blog-> {
            String b = blog.getContext();
            //消掉html标签
            b = b.replaceAll("</?[a-zA-Z]+[^><]*>","");
            if (b.length()>100)
                blog.setContext(b.substring(0,100));
            else
                blog.setContext(b);
        });
        pageInfo = new PageInfo<>(blogList);
        return ResultUtil.success(pageInfo);
    }

    private static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    @Override
    public Result selectBlogById(Integer id) {
        blogMapper.readBlog(id);
        return ResultUtil.success(blogMapper.selectByPrimaryKey(id));
    }

    @Override
    public Result deleteBlog(Integer id) {
        if (blogMapper.deleteByPrimaryKey(id) > 0) {
            return iTagService.deleteTagsByBlogId(id);
        }
        return  ResultUtil.error("删除失败");
    }

    @Override
    public Result setBlogTag(Blog blog) {
        return ResultUtil.success(blogMapper.updateByPrimaryKeySelective(blog));
    }
}
