package com.wust.myblog.shiro;

import com.wust.myblog.mapper.UserMapper;
import com.wust.myblog.modal.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 自定义Ream
 * 需重写doGetAuthenticationInfo和doGetAuthorizationInfo方法
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("user---->"+token.getUsername());
        // 从数据库获取对应用户名密码的用户
        User user = userMapper.selectByUsername(token.getUsername());
        String password = user.getPassword();
        logger.info("password----->"+ new String((char[]) token.getCredentials()));
        if (null == password) {
            throw new AccountException("用户名不正确");
        } else if (!password.equals(new String((char[]) token.getCredentials()))) {
            throw new AccountException("密码不正确");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("————权限认证————");
        String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        String role = userMapper.getRole(username);
        Set<String> set = new HashSet<>();
        //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        set.add(role);
        //设置该用户拥有的角色
        info.setRoles(set);
        return info;
    }
}
