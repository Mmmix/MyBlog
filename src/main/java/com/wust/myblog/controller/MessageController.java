package com.wust.myblog.controller;

import com.wust.myblog.modal.Message;
import com.wust.myblog.modal.Result;
import com.wust.myblog.service.IMessageService;
import com.wust.myblog.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    IMessageService iMessageService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public Result addMessage(Message message){
        return iMessageService.addMessage(message);
    }


    @RequestMapping(value = "list",method = RequestMethod.GET)
    public Result list(Integer pageNum,Integer pageSize){
        return iMessageService.selectByParent(pageNum,pageSize);
    }
}
