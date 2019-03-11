package com.wust.myblog.service;

import com.wust.myblog.modal.Message;
import com.wust.myblog.modal.Result;

public interface IMessageService {
    Result addMessage(Message message);

    Result deleteMessage(Integer id);

    Result selectById(Integer id);

    Result selectByParent(Integer pageNum,Integer pageSize);
}
