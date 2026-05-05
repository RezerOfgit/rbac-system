package com.rbac.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rbac.entity.SysUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Re-zero
 * @version 1.0
 * Spring Security 认证用户详情对象
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private SysUser user;

    // 存储该用户对应的所有权限标识 (如: ["sys:user:add", "sys:user:delete"])
    private List<String> permissions;

    public LoginUser(SysUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    /**
     * 将我们数据库里的权限标识，转换为 Security 需要的 GrantedAuthority 对象
     */
    @JsonIgnore // 序列化时忽略，避免存入 Redis 时报错
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions == null || permissions.isEmpty()) {
            return null;
        }
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 下面四个方法是账号状态校验，简单起见我们直接关联 SysUser 的 status

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true; // 账号是否未过期
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == 1; // 账号是否未锁定 (1正常 0停用)
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 密码是否未过期
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return user.getDelFlag() == 0; // 账号是否可用 (逻辑删除标志)
    }
}
