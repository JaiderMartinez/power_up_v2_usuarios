package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;

public class FactoryUserModelTest {

    public static UserModel userModel() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("+574053986322");
        userModel.setIdentificationDocument(7388348534l);
        userModel.setEmail("test@example.com");
        userModel.setRol(rolModel);
        return userModel;
    }
}
