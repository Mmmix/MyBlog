package com.wust.myblog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wust.myblog.mapper.BlogMapper;
import com.wust.myblog.mapper.CommentMapper;
import com.wust.myblog.modal.*;
import com.wust.myblog.modal.common.CommentVo;
import com.wust.myblog.service.ICommentService;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentService implements ICommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    BlogMapper blogMapper;

    @Override
    public Result addComment(Comment comment) {
        if (comment!=null&&comment.getBlogId()!=null){
            if (commentMapper.insertSelective(comment)>0) {
                blogMapper.commentBlog(comment.getBlogId());
                return ResultUtil.success("评论成功");
            }
        }
        return ResultUtil.error("评论失败");
    }

    @Override
    public Result deleteCommentsByBlogId(Integer id) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andBlogIdEqualTo(id);
        if (commentMapper.deleteByExample(commentExample) > 0){
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    @Override
    public Result deleteComment(Comment comment) {
        if (comment!=null&&comment.getId()!=null){
            if (commentMapper.deleteByPrimaryKey(comment.getId())>0)
                return ResultUtil.success();
        }
        return ResultUtil.error("删除失败");
    }

    @Override
    public Result selectCommentByBlogId(Integer id,Integer pageNum,Integer pageSize) {
        if (id!=null){
            PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?5:pageSize);
            CommentExample example = new CommentExample();
            CommentExample.Criteria criteria = example.createCriteria();
            criteria.andBlogIdEqualTo(id);
            criteria.andParentIdEqualTo(0);
            List<Comment> commentList = commentMapper.selectByExample(example);

            commentList.forEach(comment -> {
                List<Comment> childComments = new ArrayList<>();
                selectCommentByParent(comment.getId(),childComments);
                comment.setChildList(childComments);
            });
            PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);
            return ResultUtil.success(commentPageInfo);
        }
        return ResultUtil.error("查询错误");
    }

    @Override
    public Result selectCommentByParent(Comment comment) {
        List<Comment> commentList = CollectionUtil.newArrayList();
        selectCommentByParent(comment.getId(),commentList);
        return ResultUtil.success(commentList);
    }

    @Override
    public Result selectCommentAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize);
        PageInfo<Comment> commentPageInfo;
        List<Comment> commentList;

        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        commentExample.setOrderByClause("`create_time` DESC");
        commentList = commentMapper.selectByExample(commentExample);

        for (Comment comment: commentList){
            comment.setBlogTitle(blogMapper.selectByPrimaryKey(comment.getBlogId()).getTitle());
        }

        commentPageInfo = new PageInfo<>(commentList);
        return ResultUtil.success(commentPageInfo);
    }

    @Override
    public Result deleteCommentById(Integer id){
        if (id != null){
            if (commentMapper.deleteByPrimaryKey(id) > 0){
                return  ResultUtil.success("删除成功");
            }
        }
        return ResultUtil.error("删除失败");
    }

    private void selectCommentByParent(Integer parentId,List<Comment> result){
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<Comment> commentList= commentMapper.selectByExample(commentExample);
        if (commentList.size()>0){
            for (Comment commentItem:commentList){
                result.add(commentItem);
                selectCommentByParent(commentItem.getId(),result);
            }
        }
    }

}
