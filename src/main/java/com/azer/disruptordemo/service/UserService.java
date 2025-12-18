package com.azer.disruptordemo.service;

import com.azer.disruptordemo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author a1798
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-12-13 11:04:52
*/
public interface UserService extends IService<User> {

    User getLoginUser(HttpServletRequest request) throws Exception;

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request) throws Exception;

}
