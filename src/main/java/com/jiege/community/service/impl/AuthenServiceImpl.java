package com.jiege.community.service.impl;

import com.jiege.community.dao.UserDao;
import com.jiege.community.dto.LoginRequestBody;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.entity.User;
import com.jiege.community.common.ResponseCode;
import com.jiege.community.enums.UserStatus;
import com.jiege.community.common.exception.BusinessException;
import com.jiege.community.security.JwtUtil;
import com.jiege.community.service.AuthenService;
import com.jiege.community.service.UserService;
import com.jiege.community.vo.LoginVO;
import com.jiege.community.vo.UserVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Author: 19599
 * @Date: 2026/8/15 20:16
 * @Description:
 */
@Service
public class AuthenServiceImpl implements AuthenService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public AuthenServiceImpl(UserDao userDao,
                             PasswordEncoder passwordEncoder,
                             JwtUtil jwtUtil,
                             UserService userService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @Override
    public LoginVO login(LoginRequestBody request) {
        User user = userDao.selectByUsername(request.getUsername());
        if (user == null
                || !user.getStatus().equals(UserStatus.NORMAL.getStatus())
                || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResponseCode.PASSWORD_ERROR);
        }
        return new LoginVO(jwtUtil.generateToken(user.getUserId()), new UserVO(user));
    }

    @Override
    public UserVO register(UserCreateRequestBody request) {
        return userService.register(request);
    }
}
