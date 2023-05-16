package com.reto.usuario.application.mapper.response;

import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserResponseMapper {

    @Mapping(target = "rol", source = "rol.name")
    UserResponseDto toUserResponseDto(UserModel userModel);
}
