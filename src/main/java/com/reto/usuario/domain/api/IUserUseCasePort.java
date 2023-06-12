package com.reto.usuario.domain.api;

import com.reto.usuario.domain.model.UserModel;

public interface IUserUseCasePort {

    UserModel registerUserWithOwnerRole(UserModel userModel);

    UserModel registerUserWithEmployeeRole(UserModel userModel, String tokenWithBearerPrefix, Long idRestaurant);

    UserModel registerUserWithCustomerRole(UserModel userCustomerRequest);

    UserModel findUserByEmail(String email);

    UserModel getUserById(Long idUser);
}