package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;

public interface IUserService {

    UserOwnerResponseDto registerUserWithOwnerRole(UserRequestDto userRequestDto);

    void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto);

    void registerUserWithCustomerRole(CustomerRequestDto customerRequestDto);

    UserResponseDto getUserById(Long idUser);
}
