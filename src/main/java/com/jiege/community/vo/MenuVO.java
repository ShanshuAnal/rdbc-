package com.jiege.community.vo;

import com.jiege.community.entity.Menu;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:30
 * @Description: 菜单返回对象（树形结构，children 为子菜单列表）
 */
@Getter
public class MenuVO {

    /**
     * 数据库主键（自增），游标分页等场景使用
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
     * 子菜单列表（树形结构）
     */
    private List<MenuVO> children;

    public MenuVO(Menu menu) {
        this.id = menu.getId();
        this.menuId = menu.getMenuId();
        this.parentId = menu.getParentId();
        this.menuName = menu.getMenuName();
        this.path = menu.getPath();
        this.perms = menu.getPerms();
        this.type = menu.getType();
        this.icon = menu.getIcon();
        this.sort = menu.getSort();
        this.status = menu.getStatus();
        this.createTime = menu.getCreateTime();
        this.children = new ArrayList<>();
    }
}
