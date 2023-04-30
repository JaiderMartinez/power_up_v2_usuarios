package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;

public class FactoryUserModelTest {

    public static UserModel userModelFieldsEmpty() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setName("  ");
        userModel.setLastName(" ");
        userModel.setPassword("  ");
        userModel.setCellPhone(" ");
        userModel.setIdentificationDocument(7388348534l);
        userModel.setEmail("test-exception@gmail.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static UserModel userModelEmailStructureInvalid() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("3013986322");
        userModel.setIdentificationDocument(7388348534l);
        userModel.setEmail("test-exceptiongmail.com.");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static UserModel userModelCellPhoneInvalid() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("+4633013986322");
        userModel.setIdentificationDocument(7388348534l);
        userModel.setEmail("test-exception@gmail.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static UserModel userModelWithoutRole() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("+574053986322");
        userModel.setIdentificationDocument(7388348534l);
        userModel.setEmail("test-exception@gmail.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static RolModel rolModel() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        return rolModel;
    }

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
