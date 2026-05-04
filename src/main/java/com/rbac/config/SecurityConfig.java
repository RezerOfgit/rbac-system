package com.rbac.config;

import com.rbac.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Re-zero
 * @version 1.0
 * Spring Security 核心配置类
 */
@Configuration
@EnableWebSecurity
// 开启基于注解的细粒度权限控制 (如 @PreAuthorize)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 配置密码加密器 (数据库里的密码必须是密文)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 暴露 AuthenticationManager，后续在 LoginController 中需要用它来主动触发账号密码校验
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 配置 Security 过滤链规则
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭 CSRF 跨站请求伪造防护（前后端分离项目不需要，因为我们不用 Cookie 传 Token）
                .csrf().disable()

                // 2. 开启跨域支持
                .cors().and()

                // 3. 将 Session 策略设置为无状态模式（Security 不再使用 Session 保存安全上下文）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                // 4. 配置请求拦截规则
                .authorizeRequests()
                // 对于登录接口，允许匿名访问 (所有人都能点登录)
                .antMatchers("/api/auth/login").permitAll()
                // 允许前端发送跨域的 OPTIONS 预检请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他所有请求都必须认证（登录）后才能访问
                .anyRequest().authenticated();

        // 5. 将我们自定义的 JWT 过滤器，放到 Security 默认的账号密码校验过滤器之前
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
