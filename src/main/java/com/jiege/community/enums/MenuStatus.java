package com.jiege.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:30
 * @Description: 菜单状态枚举
 */
@AllArgsConstructor
@Getter
public enum MenuStatus {

    /**
     * 禁用
     */
    DISABLED(0),

    /**
     * 正常
     */
    NORMAL(1),
    ;

    /**
     * 状态值：0-禁用，1-正常
     */
    private final int status;
}
