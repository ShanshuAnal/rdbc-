package com.jiege.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/15 23:39
 * @Description: 角色状态切换请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleStatusUpdateRequestBody {

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值只能是0或1")
    @Max(value = 1, message = "状态值只能是0或1")
    private Integer status;
}
