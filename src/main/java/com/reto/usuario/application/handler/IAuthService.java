package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;

public interface IAuthService {

    public String singIn(AuthCredentialsRequest authCredentialsRequest);
}
