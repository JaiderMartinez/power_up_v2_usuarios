package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserResponseDto;

public interface IUserService {

    void registerUserWithOwnerRole(UserRequestDto userRequestDto);

    void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto, String tokenWithBearerPrefix);

    void registerUserWithCustomerRole(CustomerRequestDto customerRequestDto);

    UserResponseDto getUserById(Long idUser);
}
