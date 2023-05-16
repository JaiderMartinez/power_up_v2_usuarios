package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;

public class FactoryUserModelTest {

    public static UserModel userModelWithEmployeeRole() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(3L);
        UserModel userModel = new UserModel();
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("+574053986322");
        userModel.setIdentificationDocument(7388348534L);
        userModel.setEmail("test-exception@gmail.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static RolModel rolModelEmployee() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(3L);
        rolModel.setName("EMPLEADO");
        rolModel.setDescription("");
        return rolModel;
    }

}
