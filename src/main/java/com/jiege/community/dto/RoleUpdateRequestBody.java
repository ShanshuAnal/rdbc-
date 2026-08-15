package com.jiege.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:38
 * @Description: 修改角色请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequestBody {

    @NotBlank(message = "roleId不能为空")
    private String roleId;

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 2, max = 50, message = "角色名称长度必须在2到50个字符之间")
    private String roleName;

    @NotBlank(message = "角色标识不能为空")
    @Pattern(regexp = "^[a-z][a-z0-9:_-]*$", message = "角色标识必须以小写字母开头，只能包含小写字母、数字、冒号、下划线、中划线")
    @Size(min = 2, max = 50, message = "角色标识长度必须在2到50个字符之间")
    private String roleKey;

    @Size(max = 255, message = "角色描述不能超过255个字符")
    private String description;
}
