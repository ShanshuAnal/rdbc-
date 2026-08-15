package com.jiege.community.controller;

import com.jiege.community.dto.LoginRequestBody;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.common.HttpResponse;
import com.jiege.community.service.AuthenService;
import com.jiege.community.vo.LoginVO;
import com.jiege.community.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: 19599
 * @Date: 2026/8/15 18:04
 * @Description: 登录
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenService authenService;

    public AuthController(AuthenService authenService) {
        this.authenService = authenService;
    }


    @PostMapping("/login")
    public ResponseEntity<HttpResponse<LoginVO>> login(@RequestBody @Valid LoginRequestBody request) {
        LoginVO loginVO = authenService.login(request);
        return ResponseEntity.ok(HttpResponse.success(loginVO));
    }

    /**
     * 注册
     *
     * @param request 注册请求参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<HttpResponse<UserVO>> register(@RequestBody @Valid UserCreateRequestBody request) {
        UserVO userVO = authenService.register(request);
        return ResponseEntity.ok(HttpResponse.success(userVO));
    }
}
