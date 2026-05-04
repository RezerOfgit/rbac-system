package com.rbac.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rbac.entity.SysUser;
import com.rbac.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Re-zero
 * @version 1.0
 * 自定义用户加载服务：连接 Security 与数据库的桥梁
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    // ========== 临时调试开关 ==========
    // 当动态权限数据库联表尚未完成时，使用此开关模拟不同权限场景
    // true: admin 拥有所有权限 (*:*:*)     → 测试通过流
    // false: admin 只有 sys:user:add       → 测试越权拦截 (403)
    private static final boolean SUPER_ADMIN_MODE = true;
    // =================================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 从数据库中查询用户信息 (MyBatis-Plus 的 LambdaQuery 语法)
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)
        );

        // 2. 如果没找到用户，抛出异常
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误"); // 为了安全，不提示"用户不存在"
        }

        // 3. 查询该用户的权限列表 (今天为了测试框架连通性，先给个写死的超级管理员权限，后续会替换为连表查询)
        List<String> permissions = new ArrayList<>();

        if ("admin".equals(username)) {
            if (SUPER_ADMIN_MODE) {
                permissions.add("*:*:*"); // true：全权限
                log.info("admin 使用超级管理员模式 (*:*:*)");
            } else {
                permissions.add("sys:user:add"); // false：仅有添加权限，测试越权拦截 (403)
                log.info("admin 使用受限模式 (sys:user:add)");
            }
        }

        // 4. 将 SysUser 包装成 Security 认识的 LoginUser
        return new LoginUser(user, permissions);
    }
}
