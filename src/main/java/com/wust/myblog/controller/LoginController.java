package com.wust.myblog.controller;

import com.wust.myblog.mapper.UserMapper;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.wust.myblog.util.ResultUtil;

@CrossOrigin
@RestController
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/notLogin", method = RequestMethod.GET)
    public Result notLogin() {
        return ResultUtil.error(-1,"您尚未登陆！");
    }

    @RequestMapping(value = "/notRole", method = RequestMethod.GET)
    public Result notRole() {
        return ResultUtil.error(-1,"您没有权限！");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Result logout() {

        return iUserService.logout();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(User user) {
        Result result = iUserService.loginCheck(user);
        if (result.getCode()==200) {
            return iUserService.login(user);
        }
        return result;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(User user) {
        Result result = iUserService.validate(user);
        if (result.getCode()==200)
            return iUserService.register(user);
        return result;
    }

    @RequestMapping(value = "/actiUser",method = RequestMethod.GET)
    public Result active(String username,String token){
        return iUserService.active(username,token);
    }

    @RequestMapping(value = "/forgetPwd",method = RequestMethod.GET)
    public Result forget(String  email){
        if (StringUtils.isNoneBlank(email))
            return iUserService.forgetUser(email);
        return ResultUtil.error("请输入您的邮箱");
    }
}
