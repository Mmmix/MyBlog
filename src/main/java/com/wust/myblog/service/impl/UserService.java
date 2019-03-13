package com.wust.myblog.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.wust.myblog.mapper.UserMapper;
import com.wust.myblog.modal.Result;
import com.wust.myblog.modal.User;
import com.wust.myblog.modal.UserExample;
import com.wust.myblog.service.IUserService;
import com.wust.myblog.util.ResultUtil;
import com.wust.myblog.util.SendEmail;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService implements IUserService {


    @Autowired
    private RedisTemplate redisTemplate;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public Result login(User user) {
        if (StringUtils.isBlank(user.getUsername())){
            String username = userMapper.getUsernameByEmail(user.getEmail());
            user.setUsername(username);
        }
        User u = userMapper.selectByUsername(user.getUsername());
        if (u==null) {
            return ResultUtil.error("用户不存在");
        }else if (u.getState()==0){
            return ResultUtil.error("用户未激活");
        }

        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        //System.out.println(user.getPassword());
        // 执行认证登陆
        try {
            subject.login(token);
        }catch (Exception e){
            return ResultUtil.error(e.getMessage());
        }
        //根据权限，指定返回数据
        String role = userMapper.getRole(user.getUsername());
        String uuidtoken = UUID.randomUUID().toString();
        u.setPassword("");
        redisTemplate.opsForValue().set(uuidtoken,u);
        redisTemplate.expire(uuidtoken,10, TimeUnit.MINUTES);

        if ("user".equals(role)) {
            Map result = new HashMap();
            return ResultUtil.success(result);
        }
        if ("admin".equals(role)) {
            Map<String,Object> result = new HashMap<>();
            result.put("token",uuidtoken);
            return ResultUtil.success(result);
        }
        return ResultUtil.error(-1,"权限错误！");
    }

    @Override
    public Result logout(String token) {

        if (token!=null&&redisTemplate.opsForValue().get(token)!=null){
            Subject subject = SecurityUtils.getSubject();
            //注销
            subject.logout();
            redisTemplate.delete(token);
        }
        return ResultUtil.success("成功注销！");
    }

    @Override
    public Result register(User user) {
        String email = user.getEmail();
        String activeToken = UUID.randomUUID().toString();
        user.setActicode(activeToken);
        user.setTokenExptime(new Date());
        user.setState(0);
        user.setRole(2);
        if (userMapper.insertSelective(user)>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            StringBuffer sb=new StringBuffer("<div style=\"width:660px;overflow:hidden;border-bottom:1px solid #bdbdbe;\"><div style=\"height:52px;overflow:hidden;border:1px solid #464c51;background:#353b3f url(http://www.lofter.com/rsc/img/email/hdbg.png);\"><a href=\"http://www.lofter.com?mail=qbclickbynoticemail_20120626_01\" target=\"_blank\" style=\"display:block;width:144px;height:34px;margin:10px 0 0 20px;overflow:hidden;text-indent:-2000px;background:url(http://www.lofter.com/rsc/img/email/logo.png) no-repeat;\">LOFTER</a></div>"+"<div style=\"padding:24px 20px;\">您好，"+email+"<br/><br/>这是一款\"专注兴趣、分享创作\"的轻博客产品，旨在为\"热爱记录生活、追求时尚品质、崇尚自由空间\"的你，打造一个全新而定展示平台！<br/><br/>请点击下面链接激活账号，24小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
            sb.append("<a href=\"http://localhost:8080/actiUser?username=");
            sb.append(user.getUsername());
            sb.append("&token=");
            sb.append(activeToken);
            sb.append("\">http://localhost:8080/actiUser?username=");
            sb.append(user.getUsername());
            sb.append("&token=");
            sb.append(activeToken);
            sb.append("</a>"+"<br/>如果以上链接无法点击，请把上面网页地址复制到浏览器地址栏中打开<br/><br/><br/>LOFTER，专注兴趣，分享创作<br/>"+sdf.format(new Date())+ "</div></div>" );
            log.info(sb.toString());
            //发送邮件
            SendEmail.send(email, sb.toString());
        }
        return ResultUtil.success("注册成功，请登陆邮箱激活！");
    }

    @Override
    public Result active(String username, String token) {
        User user= userMapper.selectByUsername(username);
        if (user==null||user.getState()==1){
            return ResultUtil.error("无效操作");
        }else {
            if (user.getTokenExptime().getTime()< DateUtil.offsetDay(new Date(),-1).getTime())
                return ResultUtil.error("当前token已过期");
            if (StringUtils.equals(user.getActicode(),token)){
                user.setState(1);
                user.setTokenExptime(DateUtil.offsetDay(new Date(),-1));
                if (userMapper.updateByPrimaryKeySelective(user)>0){
                    return ResultUtil.success("激活成功！");
                }
                return ResultUtil.error("无知错误");
            }
            return ResultUtil.error("无效操作");
        }
    }

    @Override
    public Result forgetUser(String email) {
        return null;
    }

    @Override
    public Result validate(User user) {
        if (user!=null&& StringUtils.isNoneBlank(user.getUsername())){
            if (StringUtils.isNoneBlank(user.getEmail())){
                if (StringUtils.isNoneBlank(user.getPassword())){
                    UserExample userExample = new UserExample();
                    UserExample.Criteria criteria = userExample.createCriteria();
                    criteria.andUsernameEqualTo(user.getUsername());
                    if (userMapper.selectByExample(userExample).size()>0){
                        return ResultUtil.error("用户名已存在");
                    }else {
                        userExample = new UserExample();
                        criteria = userExample.createCriteria();
                        criteria.andEmailEqualTo(user.getEmail());
                        if (userMapper.selectByExample(userExample).size()>0){
                            return ResultUtil.error("邮箱已存在");
                        }{
                            return ResultUtil.success("校验成功");
                        }
                    }
                }
                return ResultUtil.error("密码不能为空！");
            }
            return ResultUtil.error("邮箱不能为空！");
        }
        return ResultUtil.error("用户名不能为空！");
    }

    public Result loginCheck(User user){
        if (user!=null){
            if( StringUtils.isBlank(user.getUsername())&&StringUtils.isBlank(user.getEmail())) {
                return ResultUtil.error("请输入用户名或邮箱");
            }if (StringUtils.isBlank(user.getPassword())){
                return ResultUtil.error("密码不能为空！");
            }
            return ResultUtil.success();
        }
        return ResultUtil.error("参数错误！");
    }
}
