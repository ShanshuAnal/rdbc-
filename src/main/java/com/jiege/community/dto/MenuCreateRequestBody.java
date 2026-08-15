package com.jiege.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/16 1:30
 * @Description: 新增菜单请求体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequestBody {

    /**
     * 父菜单业务ID，根菜单不传
     */
    private String parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 1, max = 50, message = "菜单名称长度必须在1到50个字符之间")
    private String menuName;

    /**
     * 路由地址
     */
    @Size(max = 200, message = "路由地址长度不能超过200个字符")
    private String path;

    /**
     * 权限标识，如 user:add
     */
    @Pattern(regexp = "^[a-z][a-z0-9:_-]*$", message = "权限标识格式错误")
    @Size(max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;

    /**
     * 菜单类型：1-目录，2-菜单，3-按钮
     */
    @NotNull(message = "菜单类型不能为空")
    @Min(value = 1, message = "菜单类型只能是1、2、3")
    @Max(value = 3, message = "菜单类型只能是1、2、3")
    private Integer type;

    /**
     * 图标
     */
    @Size(max = 100, message = "图标长度不能超过100个字符")
    private String icon;

    /**
     * 显示排序，默认0
     */
    private Integer sort;
}
