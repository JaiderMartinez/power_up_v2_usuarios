package com.reto.usuario.domain.api;

import com.reto.usuario.domain.model.AuthCredentialModel;

public interface IAuthUseCasePort {

    String signInUseCase(AuthCredentialModel authCredentials);
}
