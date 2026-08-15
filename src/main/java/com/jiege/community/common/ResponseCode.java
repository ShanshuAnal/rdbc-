package com.jiege.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:02
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS(0, "success"),

    /**
     * 用户相关
     */
    USERNAME_EXISTS(10001, "用户名已存在"),
    EMAIL_EXISTS(10002, "邮箱已被注册"),
    PHONE_EXISTS(10003, "手机号已被注册"),
    USER_NOT_EXISTS(10004, "用户不存在"),
    PASSWORD_ERROR(10005, "用户名或者密码错误"),

    /**
     * 角色相关
     */
    ROLE_NAME_EXISTS(10101, "角色名称已存在"),
    ROLE_KEY_EXISTS(10102, "角色标识已存在"),
    ROLE_NOT_EXISTS(10104, "角色不存在"),
    ROLE_HAS_USER(10105, "角色已绑定用户，无法删除"),

    /**
     * 菜单相关
     */
    MENU_NAME_EXISTS(10201, "菜单名称已存在"),
    MENU_PERMS_EXISTS(10202, "权限标识已存在"),
    MENU_NOT_EXISTS(10204, "菜单不存在"),
    MENU_HAS_CHILD(10205, "菜单存在子菜单，无法删除"),
    MENU_HAS_ROLE(10206, "菜单已被角色引用，无法删除"),
    MENU_PARENT_NOT_EXISTS(10207, "父菜单不存在"),


    /**
     * 参数错误
     */
    PARAM_ERROR(40000, "参数错误"),
    UNAUTHORIZED(40100, "未登录或登录已过期"),
    FORBIDDEN(40300, "没有操作权限"),
    SYSTEM_ERROR(50000, "系统异常"),

    ;


    private final int code;
    private final String message;
}
