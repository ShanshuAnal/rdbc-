package com.jiege.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:00
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum MenuType {
    DIRECTORY(1),
    MENU(2),
    BUTTON(3);

    private final int type;

}
