package com.wust.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wust.myblog.mapper.MessageMapper;
import com.wust.myblog.modal.Message;
import com.wust.myblog.modal.MessageExample;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.IMessageService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageMapper messageMapper;

    @Override
    public Result addMessage(Message message) {
        if (message!=null&&message.getContent()!=null){
            messageMapper.insertSelective(message);
            return ResultUtil.success();
        }
        return ResultUtil.error("留言失败");
    }

    @Override
    public Result deleteMessage(Integer id) {
        if (messageMapper.deleteByPrimaryKey(id)>0)
            return ResultUtil.success();
        return ResultUtil.error("失败");
    }

    @Override
    public Result selectById(Integer id) {
        return ResultUtil.success(messageMapper.selectByPrimaryKey(id));
    }

    @Override
    public Result selectByParent(Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum==null?1:pageNum,pageSize==null?10:pageSize);
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(0);
        List<Message> messageList = messageMapper.selectByExampleWithBLOBs(example);
        messageList.forEach(message -> {
            List<Message> list = new ArrayList<>();
            selectByParent(message.getId(),list);
            message.setChildMessage(list);
        });
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);
        return ResultUtil.success(pageInfo);
    }

    private void selectByParent(Integer id, List<Message> messageList){
        MessageExample example = new MessageExample();
        MessageExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<Message> list = messageMapper.selectByExampleWithBLOBs(example);
        list.forEach(message -> {
            messageList.add(message);
            selectByParent(message.getId(),messageList);
        });
    }
}
