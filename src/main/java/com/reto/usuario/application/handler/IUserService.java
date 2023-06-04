package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserEmployeeResponseDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.dto.response.UserWithFieldIdUserResponseDto;

public interface IUserService {

    UserOwnerResponseDto registerUserWithOwnerRole(UserRequestDto userRequestDto);

    UserEmployeeResponseDto registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto, String tokenWithPrefixBearer);

    UserResponseDto getUserById(Long idUser);

    UserWithFieldIdUserResponseDto getUserByUniqueEmail(String email);
}
