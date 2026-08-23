package com.jiege.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: 19599
 * @Date: 2026/8/23 3:04
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequestBody {
    private List<String> roleIds;
}
