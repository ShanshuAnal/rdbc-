package com.jiege.community.security;

import com.jiege.community.entity.User;

/**
 * @Author: 19599
 * @Date: 2026/8/15 21:34
 * @Description:
 */
public record LoginUser(String userId, String username, User user) {
}
