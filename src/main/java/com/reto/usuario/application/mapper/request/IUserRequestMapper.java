package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.UserCustomerRequestDto;
import com.reto.usuario.application.dto.request.UserEmployeeRequestDto;
import com.reto.usuario.application.dto.request.UserOwnerRequestDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IUserRequestMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol", ignore = true)
    UserModel toUserModel(UserOwnerRequestDto userRequestDto);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    UserModel userEmployeeRequestDtoUserModel(UserEmployeeRequestDto userEmployeeRequestDto);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    UserModel userCustomerRequestDtoToUserModel(UserCustomerRequestDto userCustomerRequestDto);
}
