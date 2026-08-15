package com.jiege.community.vo;

import com.jiege.community.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:12
 * @Description:
 */
@AllArgsConstructor
@Getter
public class UserVO {
    private String userId;
    private String username;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime createTime;

    public UserVO(User newUser) {
        this.userId = newUser.getUserId();
        this.username = newUser.getUsername();
        this.email = newUser.getEmail();
        this.phone = newUser.getPhone();
        this.status = newUser.getStatus();
        this.createTime = newUser.getCreateTime();
    }
}