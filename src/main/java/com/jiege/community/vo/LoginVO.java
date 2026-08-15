package com.jiege.community.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: 19599
 * @Date: 2026/8/15 20:04
 * @Description:
 */
@Data
@AllArgsConstructor
public class LoginVO {
    private String token;
    private UserVO user;
}
