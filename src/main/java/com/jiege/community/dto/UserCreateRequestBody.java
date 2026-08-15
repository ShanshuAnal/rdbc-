package com.jiege.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:11
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestBody {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 32, message = "密码长度必须在8到32个字符之间")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    private String email;

    @Pattern(
            regexp = "^1[3-9]\\d{9}$",
            message = "手机号格式错误"
    )
    private String phone;
}