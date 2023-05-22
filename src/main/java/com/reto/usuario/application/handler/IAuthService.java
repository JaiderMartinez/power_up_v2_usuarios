package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.response.TokenResponseDto;

public interface IAuthService {

    TokenResponseDto singIn(AuthCredentialsRequest authCredentialsRequest);
}
