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
    UserResponseDto userModeltoUserResponseDto(UserModel userModel);

    UserOwnerResponseDto userModelToUserOwnerResponseDto(UserModel userModel);

    UserEmployeeResponseDto userModelToUserEmployeeResponseDto(UserModel userModel);

    @Mapping(target = "rol", source = "rol.name")
    UserWithFieldIdUserResponseDto userModelToUserWithFieldIdUserResponseDto(UserModel userModel);

    UserCustomerResponseDto userModelToUserCustomerResponseDto(UserModel userModel);
}
