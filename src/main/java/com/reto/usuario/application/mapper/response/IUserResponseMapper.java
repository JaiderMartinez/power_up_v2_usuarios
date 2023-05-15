package com.reto.usuario.application.mapper.response;

import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserResponseMapper {

    @Mapping(target = "rol", source = "rol.name")
    UserResponseDto toUserResponseDto(UserModel userModel);
}
