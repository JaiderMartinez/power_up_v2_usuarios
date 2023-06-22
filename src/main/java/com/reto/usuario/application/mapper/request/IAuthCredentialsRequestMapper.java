package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.model.AuthCredentialModel;
import org.mapstruct.Mapper;

@Mapper
public interface IAuthCredentialsRequestMapper {

    AuthCredentialModel toAuthCredentialModel(AuthCredentialsRequest authCredentialsRequest);
}
