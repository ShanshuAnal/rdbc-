package com.jiege.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:35
 * @Description: 角色实体，对应 sys_role 表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    /**
     * 数据库主键（自增）
     */
    private Long id;

    /**
     * 角色业务ID，UUID
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色标识
     */
    private String roleKey;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态：0-禁用，1-正常
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
