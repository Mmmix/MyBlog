package com.wust.myblog.controller;

import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.service.IUserService;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    IUserService iUserService;

    @RequestMapping("/getUser")
    public Result getUser(){
        return ResultUtil.success((String)SecurityUtils.getSubject().getPrincipal());
    }
}
