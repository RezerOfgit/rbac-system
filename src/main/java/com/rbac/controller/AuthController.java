package com.rbac.controller;

import com.rbac.common.R;
import com.rbac.dto.LoginRequest;
import com.rbac.dto.LoginResponse;
import com.rbac.security.JwtTokenProvider;
import com.rbac.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Re-zero
 * @version 1.0
 * 认证授权接口
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {

        // 1. 将前端传来的用户名和明文密码封装成未认证的 Token 对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        // 2. 调用 AuthenticationManager 进行认证。
        // 它底层会调用我们之前写的 CustomUserDetailsService.loadUserByUsername 查询数据库，
        // 并使用 PasswordEncoder 将明文密码加密后与数据库密文进行比对。
        // 如果密码错误，这里会直接抛出异常，被我们的全局异常处理器拦截。
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 认证成功，将认证信息放入 Security 上下文（虽然登录接口放行了，但存入上下文是个好习惯）
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. 从认证信息中获取我们的 LoginUser 对象
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 5. 调用工具类生成 JWT Token
        String jwt = tokenProvider.generateToken(loginUser.getUsername());

        // 6. 构建并返回包含 Token 和权限列表的响应对象
        LoginResponse response = new LoginResponse(jwt, loginUser.getPermissions());
        return R.ok(response);
    }

    /**
     * 一个用于测试是否成功携带 Token 的受保护接口
     */
    @GetMapping("/info")
    public R<String> getUserInfo() {
        // 如果请求能走到这里，说明 JwtAuthenticationFilter 已经成功拦截并验证了 Token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return R.ok("当前已登录用户: " + currentUsername + "，你已成功访问受保护的接口！");
    }
}
