package com.jiege.community.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * @Author: 19599
 * @Date: 2026/8/15 16:27
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    
    private final ObjectMapper objectMapper;

    public WebLogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut("execution(* com.jiege.community.controller.*.*(..))")
    public void controllerPointCut() {
    }

    @Around("controllerPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request = getRequest();
        String url = request.getMethod() + " " + request.getRequestURL()
                + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
        String requestBody = serializeArgs(joinPoint.getArgs());

        try {
            Object result = joinPoint.proceed();
            log.info("【接口调用】url={}，请求体={}，耗时={}ms", url, requestBody, System.currentTimeMillis() - startTime);
            return result;
        } catch (Throwable e) {
            log.error("【接口调用异常】url={}，请求体={}，耗时={}ms}", url, requestBody, System.currentTimeMillis() - startTime);
            throw e;
        }
    }

    private String serializeArgs(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            try {
                sb.append(objectMapper.writeValueAsString(arg)).append(",");
            } catch (JacksonException e) {
                sb.append(arg.getClass().getSimpleName()).append(",");
            }
        }
        return !sb.isEmpty() ? sb.substring(0, sb.length() - 1) : "";
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }
}
