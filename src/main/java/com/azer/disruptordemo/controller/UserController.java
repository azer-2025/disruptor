package com.azer.disruptordemo.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.azer.disruptordemo.domain.User;
import com.azer.disruptordemo.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody User userLoginRequest, HttpServletRequest request) throws Exception {
        String userAccount = null;
        if (userLoginRequest != null) {
            userAccount = userLoginRequest.getUserAccount();
        }
        String userPassword = null;
        if (userLoginRequest != null) {
            userPassword = userLoginRequest.getUserPassword();
        }
        return userService.userLogin(userAccount, userPassword, request);
    }



}
