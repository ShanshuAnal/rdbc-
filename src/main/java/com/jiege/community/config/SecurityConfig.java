package com.jiege.community.config;

import com.jiege.community.entity.HttpResponse;
import com.jiege.community.enums.ResponseCode;
import com.jiege.community.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

/**
 * @Author: 19599
 * @Date: 2026/8/15 18:58
 * @Description:
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper mapper;

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(ObjectMapper mapper,
                          JwtAuthenticationFilter jwtFilter) {
        this.mapper = mapper;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 前后端分离 + JWT 无状态,不需要 CSRF
        http.csrf(AbstractHttpConfigurer::disable);
        // JWT 不用Session
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 放行规则
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/login",
                        "/auth/register")
                .permitAll()
                .anyRequest()
                .authenticated()
        );
        // 异常处理
        http.exceptionHandling(eh -> eh.authenticationEntryPoint((request, response, exception) -> {
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter()
                    .write(mapper.writeValueAsString(
                            HttpResponse.fail(ResponseCode.UNAUTHORIZED.getCode(),
                                    ResponseCode.UNAUTHORIZED.getMessage())));
        }));
        // 挂 JWT 过滤器
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
