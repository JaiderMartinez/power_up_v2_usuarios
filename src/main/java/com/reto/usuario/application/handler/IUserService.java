package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;

public interface IUserService {

    public void registerUserWithOwnerRole(UserRequestDto userRequestDto);

    void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto);
}
