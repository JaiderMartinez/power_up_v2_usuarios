package com.reto.usuario.application.mapper.request;

import com.reto.usuario.application.dto.request.UserCustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface IUserRequestMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol", ignore = true)
    UserModel toUserModel(UserRequestDto userRequestDto);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    UserModel toUserModelEmployee(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto);

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "rol.idRol", source = "idRol")
    UserModel userCustomerRequestDtoToUserModel(UserCustomerRequestDto userCustomerRequestDto);
}
