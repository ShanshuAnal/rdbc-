package com.jiege.community.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/15 16:12
 * @Description:
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo {
    @Min(value = 1, message = "页码必须从1开始")
    private int page = 1;

    @Min(value = 1, message = "每页条数至少为1")
    @Max(value = 100, message = "每页条数不能超过100")
    private int size = 10;

    /**
     * 游标：上一页最后一条记录的 id（page > 1 时必传）
     */
    @Min(value = 1, message = "游标id必须为正数")
    private Long lastId;
}

