package com.jiege.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:36
 * @Description: 角色状态枚举
 */
@AllArgsConstructor
@Getter
public enum RoleStatus {

    /**
     * 禁用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(1),
    ;

    /**
     * 状态值：0-禁用，1-启用
     */
    private final int status;
}
