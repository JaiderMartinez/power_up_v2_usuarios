package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.UserEmployeeRequestDto;
import com.reto.usuario.application.dto.request.UserOwnerRequestDto;
import com.reto.usuario.application.dto.response.UserEmployeeResponseDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.dto.response.UserWithFieldIdUserResponseDto;

public interface IUserService {

    UserOwnerResponseDto registerUserWithOwnerRole(UserOwnerRequestDto userRequestDto);

    UserEmployeeResponseDto registerUserWithEmployeeRole(UserEmployeeRequestDto userRequestToCreateEmployeeDto, String tokenWithPrefixBearer);

    UserResponseDto getUserById(Long idUser);

    UserWithFieldIdUserResponseDto getUserByUniqueEmail(String email);
}
