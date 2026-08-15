package com.jiege.community.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 16:58
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum UserStatus {

    ABANDON(0),
    NORMAL(1),
    DELETE(2),
    ;

    private final int status;
}
