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
        userModel.setIdentificationDocument(7388348534L);
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
        userModel.setIdentificationDocument(7388348534L);
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
        userModel.setIdentificationDocument(7388348534L);
        userModel.setEmail("test-exception@gmail.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static UserModel userModelWithOwnerRole() {
        RolModel rolModel = new RolModel();
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
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

    public static UserModel userModelWithCustomerRole() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(4L);
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

    public static RolModel rolModel() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(2L);
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        return rolModel;
    }

    public static RolModel rolModelEmployee() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(3L);
        rolModel.setName("EMPLEADO");
        rolModel.setDescription("");
        return rolModel;
    }

    public static RolModel rolModelCustomer() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(4L);
        rolModel.setName("CLIENTE");
        rolModel.setDescription("");
        return rolModel;
    }

    public static UserModel userModel() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(2L);
        rolModel.setName("PROPIETARIO");
        rolModel.setDescription("");
        UserModel userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setName("Luis");
        userModel.setLastName("Martinez");
        userModel.setPassword("12345678");
        userModel.setCellPhone("+574053986322");
        userModel.setIdentificationDocument(7388348534L);
        userModel.setEmail("test@example.com");
        userModel.setRol(rolModel);
        return userModel;
    }

    public static UserModel userModelWithEmployeeRoleThatDoesNotExist() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(0L);
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

}
