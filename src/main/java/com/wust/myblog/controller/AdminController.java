package com.wust.myblog.controller;

import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "admin")
@RestController
public class AdminController {
    @RequestMapping(value = "getAdmin")
    public Result getAdmin(){
        return ResultUtil.success(SecurityUtils.getSubject().getSession().getAttribute("currentUserId"));
    }
}
