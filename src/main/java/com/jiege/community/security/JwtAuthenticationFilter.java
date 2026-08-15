package com.jiege.community.security;

import com.jiege.community.dao.UserDao;
import com.jiege.community.entity.User;
import com.jiege.community.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器:每个请求进入 Controller 前先经过这里
 * <p>
 * 职责:解析 Authorization 头中的 Bearer token → 查库验证用户 → 认证通过则把 LoginUser
 * 放入 SecurityContext。验证失败一律"静默放行"(不放认证信息),拒绝动作交给
 * SecurityConfig 的 authorizeHttpRequests + AuthenticationEntryPoint 完成,401 响应格式才统一。
 *
 * @Author: 19599
 * @Date: 2026/8/15 21:03
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDao userDao;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   UserDao userDao) {
        this.jwtUtil = jwtUtil;
        this.userDao = userDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 取 token:没有 Authorization 头或格式不符 → 视为匿名请求,直接放行
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            // 剥掉 "Bearer " 前缀(共 7 个字符),剩下纯 token
            String token = authorization.substring(7);
            // 2. 解析 token:签名非法/已过期/格式错误会抛 JwtException(这里必须 try-catch 包住,
            //    否则伪造 token 会直接 500,拿不到 401 —— 修复时 catch (JwtException | IllegalArgumentException e) 静默即可)
            Claims claims = null;
            try {
                claims = jwtUtil.parseToken(token);

                // subject 里存的是 userId(JwtUtil.generateToken 时写入)
                String userId = claims.getSubject();
                // 3. 查库验证:用户被删除/禁用后 token 虽在有效期内,这里也能立刻拦截(禁用立即生效)
                User user = userDao.selectByUserId(userId);
                if (user != null && UserStatus.NORMAL.getStatus() == user.getStatus()) {
                    // 4. 认证通过:principal 放 LoginUser,Controller 可用 @AuthenticationPrincipal 取到
                    LoginUser loginUser = new LoginUser(userId, user.getUsername(), user);
                    // todo authorities —— 权限列表,现在空 List.of(),阶段 5 做角色权限时就在这里填
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginUser, null, List.of());
                    // 放入 SecurityContextHolder(本质是 ThreadLocal,请求结束自动清理)
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException | IllegalArgumentException e) {
                log.warn("Invalid JWT token: {}", e.getMessage());
            }
        }
        // 5. 无论如何都放行:未认证的请求走到 SecurityConfig 的 anyRequest().authenticated()
        //    会被 AuthenticationEntryPoint 统一返回 401
        filterChain.doFilter(request, response);
    }
}
