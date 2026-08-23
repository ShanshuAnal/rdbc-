package com.jiege.community.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/23 3:27
 * @Description:
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class UserRole {
    private Long id;
    private String userId;
    private String roleId;
}
