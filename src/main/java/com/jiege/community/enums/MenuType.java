package com.jiege.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:00
 * @Description: 菜单类型枚举
 */
@AllArgsConstructor
@Getter
public enum MenuType {

    /**
     * 目录
     */
    DIRECTORY(1),

    /**
     * 菜单
     */
    MENU(2),

    /**
     * 按钮
     */
    BUTTON(3);

    /**
     * 类型值：1-目录，2-菜单，3-按钮
     */
    private final int type;

}
