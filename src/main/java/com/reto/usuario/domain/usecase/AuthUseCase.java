package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.api.IAuthUseCasePort;
import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

public class AuthUseCase implements IAuthUseCasePort {

    private final IUserPersistenceDomainPort userPersistenceDomainPort;

    public AuthUseCase(IUserPersistenceDomainPort userPersistenceDomainPort) {
        this.userPersistenceDomainPort = userPersistenceDomainPort;
    }

    @Override
    public String signInUseCase(AuthCredentials authCredentials) {
        UserModel user = userPersistenceDomainPort.findByEmail( authCredentials.getEmail() );
        List<String> authority = new ArrayList<>();
        authority.add("ROLE_" + user.getRol().getName());
        return TokenUtils
                .createToken( user.getEmail(), authority, user.getName(), user.getLastName() );
    }
}
