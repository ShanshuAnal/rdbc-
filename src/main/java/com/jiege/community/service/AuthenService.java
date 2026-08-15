package com.jiege.community.service;

import com.jiege.community.dto.LoginRequestBody;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.vo.LoginVO;
import com.jiege.community.vo.UserVO;

/**
 * @Author: 19599
 * @Date: 2026/8/15 20:16
 * @Description:
 */
public interface AuthenService {

    /**
     * 登录
     *
     * @param request 登录请求参数
     * @return
     */
    LoginVO login(LoginRequestBody request);

    /**
     * 注册
     *
     * @param request 注册请求参数
     * @return 注册结果
     */
    UserVO register(UserCreateRequestBody request);
}
