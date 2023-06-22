package com.reto.usuario.infrastructure.drivenadapter.token;

import com.reto.usuario.domain.spi.persistence.TokenServiceInterfacePort;
import com.reto.usuario.infrastructure.configurations.security.utils.TokenUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TokenServiceAdapter implements TokenServiceInterfacePort {

    private final TokenUtils tokenUtils;

    @Override
    public String generateTokenAccess(String email, List<String> authority, String nameFromUser, String lastNameFromUser) {
        return this.tokenUtils.createToken(email, authority, nameFromUser, lastNameFromUser);
    }
}
