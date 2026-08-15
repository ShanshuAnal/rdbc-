package com.jiege.community.controller;

import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.dto.UserUpdateRequestBody;
import com.jiege.community.entity.HttpResponse;
import com.jiege.community.service.UserService;
import com.jiege.community.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/15 4:52
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 添加用户
     *
     * @param requestBody 用户信息
     * @return 响应
     */
    @PostMapping
    public ResponseEntity<HttpResponse<UserVO>> addUser(@RequestBody @Valid UserCreateRequestBody requestBody) {
        UserVO userVO = userService.addUser(requestBody);
        return ResponseEntity.ok(HttpResponse.success(userVO));
    }

    /**
     * 获取用户
     *
     * @param userId userId
     * @return 响应
     */
    @GetMapping("/{userId}")
    public ResponseEntity<HttpResponse<UserVO>> getUser(@PathVariable String userId) {
        UserVO userVO = userService.getUserById(userId);
        return ResponseEntity.ok(HttpResponse.success(userVO));
    }

    /**
     * 修改用户
     *
     * @param requestBody UserUpdateRequestBody 修改信息
     * @return 响应
     */
    @PutMapping
    public ResponseEntity<HttpResponse<UserVO>> updateUser(@RequestBody @Valid UserUpdateRequestBody requestBody) {
        UserVO userVO = userService.updateUser(requestBody);
        return ResponseEntity.ok(HttpResponse.success(userVO));
    }

    /**
     * 删除用户
     *
     * @param userId userId
     * @return 响应
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(
                HttpResponse.success()
        );
    }

    /**
     * 查询用户列表
     *
     * @param pageInfo 分页信息
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<HttpResponse<List<UserVO>>> listUsers(@RequestBody @Valid PageInfo pageInfo) {
        List<UserVO> users = userService.getUserList(pageInfo);
        return ResponseEntity.ok(HttpResponse.success(users));
    }
}
