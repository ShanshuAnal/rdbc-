package com.jiege.community.common.exception;

import com.jiege.community.common.ResponseCode;
import lombok.Getter;

/**
 * @Author: 19599
 * @Date: 2026/8/15 15:05
 * @Description:
 */
@Getter
public class BusinessException extends RuntimeException{
    /**
     * 业务状态码
     */
    private final int code;

    /**
     * 使用统一响应码
     */
    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    /**
     * 使用统一响应码，但自定义错误信息
     */
    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
    }

    /**
     * 完全自定义业务异常
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 保留原始异常堆栈
     */
    public BusinessException(ResponseCode responseCode, Throwable cause) {
        super(responseCode.getMessage(), cause);
        this.code = responseCode.getCode();
    }

    /**
     * 自定义消息 + 保留原始异常堆栈
     */
    public BusinessException(ResponseCode responseCode,
                             String message,
                             Throwable cause) {
        super(message, cause);
        this.code = responseCode.getCode();
    }

}
