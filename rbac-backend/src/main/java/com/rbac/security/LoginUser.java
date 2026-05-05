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
 * Security 认证用户详情，封装用户信息及权限列表，
 * 实现 {@link UserDetails} 以适配 Security 认证流程。
 * @author Re-zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private SysUser user;

    /** 权限标识列表，如 sys:user:add、sys:user:delete */
    private List<String> permissions;

    public LoginUser(SysUser user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    /** 将权限标识列表转换为 Security 所需的 GrantedAuthority 集合 */
    @JsonIgnore
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

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == 1;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return user.getDelFlag() == 0;
    }
}
