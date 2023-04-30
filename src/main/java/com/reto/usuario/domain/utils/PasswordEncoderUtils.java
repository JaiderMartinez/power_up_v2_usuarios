package com.reto.usuario.domain.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderUtils {

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
