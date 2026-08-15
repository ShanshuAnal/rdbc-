package com.jiege.community.common.utils;

import com.jiege.community.entity.User;
import com.jiege.community.common.ResponseCode;
import com.jiege.community.common.exception.BusinessException;
import com.jiege.community.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: 19599
 * @Date: 2026/8/16 4:33
 * @Description:
 */
public class UserContext {

    private UserContext() {
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(ResponseCode.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser loginUser) {
            return loginUser.user();
        }
        throw new BusinessException(ResponseCode.UNAUTHORIZED);
    }

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    public static String getUserId() {
        return getUser().getUserId();
    }
}
