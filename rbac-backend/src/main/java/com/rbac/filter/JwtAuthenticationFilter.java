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
 * JWT 认证过滤器。
 * 继承 OncePerRequestFilter，保证单次请求链路中该过滤器仅执行一次。
 * 负责从请求头中解析 JWT，验证其有效性，并将认证信息写入 SecurityContextHolder。
 * @author Re-zero
 * @version 1.0
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
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                String username = tokenProvider.getUsernameFromToken(jwt);

                // 加载用户信息（含权限列表）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 构造 Authentication 对象并写入 SecurityContextHolder，
                // Security 框架据此判定当前请求已完成身份认证
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("无法在 Security Context 中设置用户认证信息", e);
        }

        // 请求必须继续传递，后续由 Security 框架完成鉴权决策
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
