package com.reto.usuario.domain.api;

import com.reto.usuario.domain.dto.AuthCredentials;

public interface IAuthUseCasePort {
    String signInUseCase(AuthCredentials authCredentials);
}
