package com.reto.usuario.infrastructure.configurations.security;

import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserUseCasePort userUseCasePort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userUseCasePort.findUserByEmail(username);
        List<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority("ROLE_" + userModel.getRol().getName()));
        return new UserDetailsImpl(userModel.getEmail(), userModel.getPassword(), authority);
    }

    public boolean isValidateRoles(String email, String rol) {
        UserModel userModel = userUseCasePort.findUserByEmail(email);
        return userModel.getRol().getName().equals(rol);
    }
}
