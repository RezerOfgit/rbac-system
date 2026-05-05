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
 * 自定义用户加载服务 (完全体)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    // 注入菜单服务，用于查询真实权限
    private final SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户本体
        SysUser user = sysUserMapper.selectOne(
                new QueryWrapper<SysUser>().eq("username", username)
        );

        if (user == null) {
            log.warn("登录失败，找不到用户名: {}", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 2. 动态加载该用户的真实权限列表
        List<String> permissions = new ArrayList<>();

        // 3. 企业级标准逻辑：超级管理员放行，普通用户查库
        if ("admin".equals(user.getUsername())) {
            permissions.add("*:*:*");
            log.info("超级管理员 [admin] 登录，赋予全局通配符权限");
        } else {
            // 调用我们刚刚写的连表查询，获取该普通用户真实被分配的权限
            permissions = sysMenuService.getPermsByUserId(user.getId());
            log.info("普通用户 [{}] 登录，从数据库成功加载 {} 条权限", username, permissions.size());
        }

        // 4. 将查到的用户信息和真实权限列表打包成 Security 需要的 LoginUser
        return new LoginUser(user, permissions);
    }
}