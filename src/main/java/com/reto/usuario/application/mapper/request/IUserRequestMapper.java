package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol", ignore = true)
    UserModel toUserModel(UserRequestDto userRequestDto);
}
