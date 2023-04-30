package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.dto.AuthCredentials;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAuthCredentialsRequestMapper {

    AuthCredentials toAuthCredentials(AuthCredentialsRequest authCredentialsRequest);
}
