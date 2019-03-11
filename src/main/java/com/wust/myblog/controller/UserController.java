package com.wust.myblog.controller;

import cn.hutool.core.util.StrUtil;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.service.IUserService;
import com.wust.myblog.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

    @CrossOrigin
    @RestController
    @RequestMapping("user")
    public class UserController {


        @Autowired
        RedisTemplate redisTemplate;

        @Autowired
        IUserService iUserService;

        @RequestMapping(value = "getUser",method = RequestMethod.GET)
        public Result getUser(String token){
            if (StrUtil.isBlank(token)){
                return ResultUtil.error("无token");
            }
            redisTemplate.opsForValue().get(token);
            User  user = (User) redisTemplate.opsForValue().get(token);
            if (user==null){
                return ResultUtil.error("token无效");
            }
            return ResultUtil.success(user);
        }
}
