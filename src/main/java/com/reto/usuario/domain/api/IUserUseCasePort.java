package com.reto.usuario.domain.api;

import com.reto.usuario.domain.model.UserModel;

public interface IUserUseCasePort {

    void registerUserWithOwnerRole(UserModel userModel);

    void registerUserWithEmployeeRole(UserModel userModel);

    UserModel findUserByEmail(String email);

    UserModel getUserById(Long idUser);
}
