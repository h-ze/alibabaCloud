package com.hz.common.auth;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 */
@Data
public class SecurityUser implements UserDetails {

    /**
     * ID
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 权限数据,含有角色和资源url的权限
     */
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser() {

    }

    public SecurityUser(UserDto userDTO) {
        this.setId(userDTO.getUserId());
        this.setUsername(userDTO.getUsername());
        this.setPassword(userDTO.getPassword());
        this.setEnabled(userDTO.getStatus() == 1);
        if (userDTO.getRoles() != null) {
            authorities = new ArrayList<>();
            /*userDTO.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));*/
            userDTO.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(JSON.toJSONString(item))));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
