package com.reto.usuario.application.mapper.response;

import com.reto.usuario.application.dto.response.UserCustomerResponseDto;
import com.reto.usuario.application.dto.response.UserEmployeeResponseDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.dto.response.UserWithFieldIdUserResponseDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IUserResponseMapper {

    @Mapping(target = "rol", source = "rol.name")
    UserResponseDto toUserResponseDto(UserModel userModel);

    UserOwnerResponseDto toUserOwnerResponseDto(UserModel userModel);

    UserEmployeeResponseDto toUserEmployeeResponseDto(UserModel userModel);

    @Mapping(target = "rol", source = "rol.name")
    UserWithFieldIdUserResponseDto toUserWithFieldIdUserResponseDto(UserModel userModel);

    UserCustomerResponseDto userModeltoUserCustomerResponseDto(UserModel userModel);
}
