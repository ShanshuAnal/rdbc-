package com.jiege.community.vo;

import com.jiege.community.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:40
 * @Description: 角色返回对象
 */
@AllArgsConstructor
@Getter
public class RoleVO {

    private Long id;

    private String roleId;

    private String roleName;

    private String roleKey;

    private String description;

    private Integer status;

    private LocalDateTime createTime;

    public RoleVO(Role role) {
        this.id = role.getId();
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
        this.roleKey = role.getRoleKey();
        this.description = role.getDescription();
        this.status = role.getStatus();
        this.createTime = role.getCreateTime();
    }
}
