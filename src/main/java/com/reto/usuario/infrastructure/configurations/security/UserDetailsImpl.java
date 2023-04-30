package com.reto.usuario.infrastructure.configurations.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private String email;
    private String key;
    private List<GrantedAuthority> rol;

    public UserDetailsImpl() {}

    public UserDetailsImpl(String username, String key, List<GrantedAuthority> rol) {
        this.email = username;
        this.key = key;
        this.rol = rol;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<GrantedAuthority> getRol() {
        return rol;
    }

    public void setRol(List<GrantedAuthority> rol) {
        this.rol = rol;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rol;
    }

    @Override
    public String getPassword() {
        return this.key;
    }

    @Override
    public String getUsername() {
        return this.email;
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
