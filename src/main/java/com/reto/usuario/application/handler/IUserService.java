package com.reto.usuario.application.handler;

import com.reto.usuario.application.dto.request.UserRequestDto;

public interface IUserService {

    public void registerUserWithOwnerRole(UserRequestDto userRequestDto);

}
