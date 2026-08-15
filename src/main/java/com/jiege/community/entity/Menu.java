package com.jiege.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:30
 * @Description: 菜单实体，对应 sys_menu 表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu {

    /**
     * 数据库主键（自增）
     */
    private Long id;

    /**
     * 菜单业务ID，UUID
     */
    private String menuId;

    /**
     * 父菜单业务ID，根菜单为NULL
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 权限标识，如 user:add
     */
    private String perms;

    /**
     * 菜单类型：1-目录，2-菜单，3-按钮
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 显示排序
     */
    private Integer sort;

    /**
     * 菜单状态：0-禁用，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
