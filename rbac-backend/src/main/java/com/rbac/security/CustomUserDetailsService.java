package com.rbac.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rbac.entity.SysUser;
import com.rbac.mapper.SysUserMapper;
import com.rbac.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户加载服务，实现 {@link UserDetailsService}。
 * 根据用户名查询用户信息及权限列表，封装为 {@link LoginUser} 返回给 Security 框架。
 * @author Re-zero
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    private final SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().eq("username", username)
        );

        if (user == null) {
            log.warn("登录失败，找不到用户名: {}", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        List<String> permissions = new ArrayList<>();

        // 超级管理员拥有全局通配权限，普通用户从数据库查询
        if ("admin".equals(user.getUsername())) {
            permissions.add("*:*:*");
            log.info("超级管理员 [admin] 登录，赋予全局通配符权限");
        } else {
            permissions = sysMenuService.getPermsByUserId(user.getId());
            log.info("普通用户 [{}] 登录，从数据库成功加载 {} 条权限", username, permissions.size());
        }

        return new LoginUser(user, permissions);
    }
}