package com.jiege.community.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:03
 * @Description: 统一 HTTP 响应对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class HttpResponse<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 响应体
     */
    private T data;

    public static <T> HttpResponse<T> success() {
        return new HttpResponse<>(200, "success", null);
    }


    public static <T> HttpResponse<T> success(T data) {
        return new HttpResponse<>(200, "success", data);
    }


    public static <T> HttpResponse<T> success(String message, T data) {
        return new HttpResponse<>(200, message, data);
    }

    public static <T> HttpResponse<T> fail(int code, String message) {
        return new HttpResponse<>(code, message, null);
    }

}
