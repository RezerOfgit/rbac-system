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
 * 认证授权接口，提供登录和 Token 验证测试端点。
 * @author Re-zero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * 用户登录。
     * 由 AuthenticationManager 委托给 CustomUserDetailsService 加载用户并比对密码，
     * 认证通过后签发 JWT 返回。
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        // AuthenticationManager 内部完成密码比对，失败时抛出异常
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        String jwt = tokenProvider.generateToken(loginUser.getUsername());

        LoginResponse response = new LoginResponse(jwt, loginUser.getPermissions());
        return R.ok(response);
    }

    /**
     * 测试接口，验证请求是否成功携带有效 Token。
     */
    @GetMapping("/info")
    public R<String> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return R.ok("当前已登录用户: " + currentUsername + "，你已成功访问受保护的接口！");
    }
}
