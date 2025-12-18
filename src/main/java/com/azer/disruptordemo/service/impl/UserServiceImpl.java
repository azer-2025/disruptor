package com.azer.disruptordemo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.azer.disruptordemo.domain.User;
import com.azer.disruptordemo.service.UserService;
import com.azer.disruptordemo.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* @author a1798
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-12-13 11:04:52
*/

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public User getLoginUser(HttpServletRequest request) throws Exception {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute("user_login");
        User currentUser = (User) userObj;
        log.info("currentUser: {}", currentUser);
        if (currentUser == null || currentUser.getUserId() == null) {
            return null;
        }
        // 从数据库中查询（追求性能的话可以注释，直接返回上述结果）
        Long userId = currentUser.getUserId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            return null;
        }
        return currentUser;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) throws Exception {
        // 3. 查询数据库中的用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", userPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 不存在，抛异常
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new Exception("用户不存在或者密码错误");
        }
        // 4. 保存用户的登录态
        request.getSession().setAttribute("user_login", user);
        return user;
    }
}




