package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.response.TokenResponseDto;

public interface IAuthService {

    public TokenResponseDto singIn(AuthCredentialsRequest authCredentialsRequest);
}
