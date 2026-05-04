package com.rbac.filter;

import com.rbac.security.CustomUserDetailsService;
import com.rbac.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Re-zero
 * @version 1.0
 * JWT 核心拦截器
 * 继承 OncePerRequestFilter，确保在一次请求中只通过一次该过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. 从请求头中获取 JWT Token
            String jwt = getJwtFromRequest(request);

            // 2. 如果 Token 存在且验证合法
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                // 3. 解析 Token 获取用户名
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 4. 从数据库加载用户详细信息（包含权限）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. 将用户信息封装成 Security 认识的 Authentication 对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 记录请求的详细信息（如 IP 地址等）
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. 将 Authentication 存入 SecurityContextHolder
                // 这步极其关键，Security 只要在 Context 中看到了这个对象，就认为当前请求已经登录验证通过了
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("无法在 Security Context 中设置用户认证信息", e);
        }

        // 7. 无论有没有解析出 Token，都必须放行给下一个过滤器（交给 Security 去做最终裁决）
        filterChain.doFilter(request, response);
    }

    /**
     * 提取请求头中的 Token
     * 前端规范：Authorization: Bearer <token_string>
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // 检查头信息是否以 "Bearer " 开头
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 截取掉 "Bearer " 前缀，只保留纯 Token
        }
        return null;
    }
}
