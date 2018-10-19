package com.wust.myblog.service.impl;

import com.wust.myblog.mapper.TagMapper;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.Tag;
import com.wust.myblog.modal.TagExample;
import com.wust.myblog.service.ITagService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService implements ITagService {

    @Autowired
    TagMapper tagMapper;

    @Override
    public Result addTag(Tag tag) {
        if (tag == null || tag.getName() == null)
            return ResultUtil.error("参数错误");
        if (tagMapper.insertSelective(tag) > 0)
            return ResultUtil.success("添加分类成功！");
        return ResultUtil.error("添加分类失败！");
    }

    @Override
    public Result deleteTag(Tag tag) {
        if (tag.getId() == null)
            return ResultUtil.error("参数错误");
        TagExample tagExample = new TagExample();
        TagExample.Criteria criteria = tagExample.createCriteria();
        criteria.andIdEqualTo(tag.getId());
        criteria.andNameEqualTo(tag.getName());
        if (tagMapper.deleteByExample(tagExample) > 0)
            return ResultUtil.success("删除成功！");
        return ResultUtil.error("删除失败！");
    }

    @Override
    public Result selectTagById(Integer id) {
        Tag tag = tagMapper.selectByPrimaryKey(id);
        if (tag != null)
            return ResultUtil.success(tag);
        return ResultUtil.error("分类不存在");
    }

    @Override
    public Result selectAll() {
        List<Tag> tagList = new ArrayList<>();
        tagList = tagMapper.selectByExample(null);
        if (tagList.size() > 0)
            return ResultUtil.success(tagList);
        return ResultUtil.success();
    }

    @Override
    public Result updateTag(Tag tag) {
        if (tag == null || tag.getId() == null)
            return ResultUtil.error("参数错误");
        if (tagMapper.selectByPrimaryKey(tag.getId()) != null) {
            if (tagMapper.updateByPrimaryKeySelective(tag) > 0)
                return ResultUtil.success("更新分类成功！");
        } else {
            if (tagMapper.insertSelective(tag) > 0)
                return ResultUtil.success("更新分类成功");
        }
        return ResultUtil.error("更新分类失败！");
    }
}
