package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.api.IAuthUseCasePort;
import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.spi.TokenServiceInterfacePort;

import java.util.ArrayList;
import java.util.List;

public class AuthUseCase implements IAuthUseCasePort {

    private final IUserPersistenceDomainPort userPersistenceDomainPort;
    private final TokenServiceInterfacePort tokenService;

    public AuthUseCase(IUserPersistenceDomainPort userPersistenceDomainPort, TokenServiceInterfacePort tokenService) {
        this.userPersistenceDomainPort = userPersistenceDomainPort;
        this.tokenService = tokenService;
    }

    @Override
    public String signInUseCase(AuthCredentials authCredentials) {
        UserModel user = userPersistenceDomainPort.findByEmail( authCredentials.getEmail() );
        List<String> authority = new ArrayList<>();
        authority.add("ROLE_" + user.getRol().getName());
        return this.tokenService.generateTokenAccess(user.getEmail(), authority, user.getName(), user.getLastName());
    }
}
