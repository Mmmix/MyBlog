package com.wust.myblog.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wust.myblog.mapper.CommentMapper;
import com.wust.myblog.modal.Comment;
import com.wust.myblog.modal.CommentExample;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.service.ICommentService;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService implements ICommentService {

    @Autowired
    CommentMapper commentMapper;

    @Override
    public Result addComment(Comment comment) {
        if (comment!=null&&comment.getBlogId()!=null){
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            comment.setUserId(Integer.valueOf(user.getId().toString()));
            if (commentMapper.insertSelective(comment)>0) {
                return ResultUtil.success("评论成功");
            }
        }
        return ResultUtil.error("评论失败");
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
    public Result selectCommentByBlogId(Integer id) {
        if (id!=null){
            CommentExample example = new CommentExample();
            CommentExample.Criteria criteria = example.createCriteria();
            criteria.andBlogIdEqualTo(id);
            criteria.andParentIdEqualTo(0);
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            criteria.andUserIdEqualTo(Integer.valueOf(user.getId().toString()));
            List<Comment> list = commentMapper.selectByExample(example);
            return ResultUtil.success(list);
        }
        return ResultUtil.error("查询错误");
    }

    @Override
    public Result selectCommentByParent(Comment comment) {
        List<Comment> commentList = CollectionUtil.newArrayList();
        selectCommentByParent(comment.getId(),commentList);
        return ResultUtil.success(commentList);
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


    @Override
    public Result selectCommentByUserId() {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        Long id = ((User)(SecurityUtils.getSubject().getPrincipal())).getId();
        criteria.andUserIdEqualTo(Integer.valueOf(id.toString()));
        List<Comment> commentList= commentMapper.selectByExample(commentExample);
        return ResultUtil.success(commentList);
    }
}
