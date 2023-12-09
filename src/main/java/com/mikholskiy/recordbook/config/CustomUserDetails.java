package com.mikholskiy.recordbook.config;


import com.mikholskiy.recordbook.entity.User;
import com.mikholskiy.recordbook.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private UserRole role;

    public CustomUserDetails(User userCredential) {
        this.username = userCredential.getEmail();
        this.password = userCredential.getPassword();
        this.role = userCredential.getRole();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(userCredential.getRole().toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }
}

