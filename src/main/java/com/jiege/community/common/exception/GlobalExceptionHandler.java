package com.jiege.community.common.exception;

import com.jiege.community.common.HttpResponse;
import com.jiege.community.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 19599
 * @Date: 2026/8/15 5:03
 * @Description: 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常：HTTP 200 + body 业务码，前端统一读 code 判断
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<HttpResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常：code={}，message={}", e.getCode(), e.getMessage());
        return ResponseEntity.ok(HttpResponse.fail(e.getCode(), e.getMessage()));
    }

    /**
     * @Valid 参数校验失败：返回第一条字段校验消息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse<Void>> handleValidException(MethodArgumentNotValidException e) {
        String message = ResponseCode.PARAM_ERROR.getMessage();
        if (!e.getBindingResult().getFieldErrors().isEmpty()) {
            String defaultMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
            message = defaultMessage == null ? message : defaultMessage;
        }
        log.warn("参数校验失败：{}", message);
        return ResponseEntity.badRequest()
                .body(HttpResponse.fail(ResponseCode.PARAM_ERROR.getCode(), message));
    }

    /**
     * 请求体缺失或 JSON 解析失败
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponse<Void>> handleMessageNotReadable(HttpMessageNotReadableException e) {
        log.warn("请求体解析失败：{}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(HttpResponse.fail(ResponseCode.PARAM_ERROR.getCode(), "请求体格式错误或缺失"));
    }

    /**
     * 权限不足（后续 @PreAuthorize 校验失败时会走到这里）
     */
    //@ExceptionHandler(AccessDeniedException.class)
    //public ResponseEntity<HttpResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {
    //    log.warn("权限不足：{}", e.getMessage());
    //    return ResponseEntity.status(HttpStatus.FORBIDDEN)
    //            .body(HttpResponse.fail(ResponseCode.FORBIDDEN.getCode(), ResponseCode.FORBIDDEN.getMessage()));
    //}

    /**
     * 兜底异常：记录完整堆栈，不向客户端暴露内部细节
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<Void>> handleException(Exception e) {
        log.error("系统异常", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HttpResponse.fail(ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMessage()));
    }
}
