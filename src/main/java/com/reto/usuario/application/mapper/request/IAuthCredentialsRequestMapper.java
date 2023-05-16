package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.dto.AuthCredentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAuthCredentialsRequestMapper {

    AuthCredentials toAuthCredentials(AuthCredentialsRequest authCredentialsRequest);
}
