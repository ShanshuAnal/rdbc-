package com.jiege.community.controller;

import com.jiege.community.dto.PageInfo;
import com.jiege.community.dto.UserCreateRequestBody;
import com.jiege.community.dto.UserUpdateRequestBody;
import com.jiege.community.common.HttpResponse;
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
     * 查询用户列表（分页参数走 query string：GET 请求不能带 body，Tomcat 会丢弃）
     *
     * @param page   页码，从 1 开始
     * @param size   每页条数，默认 10
     * @param lastId 游标：上一页最后一条记录的 id，page > 1 时必传
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<HttpResponse<List<UserVO>>> listUsers(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) Long lastId) {
        PageInfo pageInfo = new PageInfo(page, size, lastId);
        List<UserVO> users = userService.getUserList(pageInfo);
        return ResponseEntity.ok(HttpResponse.success(users));
    }
}
