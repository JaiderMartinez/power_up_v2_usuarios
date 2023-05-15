package com.reto.usuario.infrastructure.entrypoint.factory;

import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import com.reto.usuario.infrastructure.drivenadapter.entity.UserEntity;

public class FactoryEntityAndRequest {

    public  static RolEntity roleEntityAdmin() {
        RolEntity rolEntityWithRoleAdmin = new RolEntity();
        rolEntityWithRoleAdmin.setIdRol(1L);
        rolEntityWithRoleAdmin.setDescription("In charge of creating restaurants and restaurant owner accounts");
        rolEntityWithRoleAdmin.setName("ADMINISTRADOR");
        return rolEntityWithRoleAdmin;
    }

    public static UserEntity userAdmin() {
        UserEntity userEntityAdmin = new UserEntity();
        userEntityAdmin.setIdUser(1L);
        userEntityAdmin.setName("Name Admin");
        userEntityAdmin.setLastName("LastName Admin");
        userEntityAdmin.setIdentificationDocument(325235325L);
        userEntityAdmin.setCellPhone("5463679324");
        userEntityAdmin.setEmail("admin@admin.com");
        userEntityAdmin.setPassword("123");
        userEntityAdmin.setRol(roleEntityAdmin());
        return userEntityAdmin;
    }

    public static UserEntity userOwner() {
        UserEntity userEntityAdmin = new UserEntity();
        userEntityAdmin.setIdUser(2L);
        userEntityAdmin.setName("Name Owner");
        userEntityAdmin.setLastName("LastName Owner");
        userEntityAdmin.setIdentificationDocument(325235325L);
        userEntityAdmin.setCellPhone("5463679324");
        userEntityAdmin.setEmail("owner@owner.com");
        userEntityAdmin.setPassword("$2a$12$4o2roUXkPtsy5umeBGoLfuU2SWrRyLj4jZwFmYUgJCtMIPutO94ZO");
        userEntityAdmin.setRol(roleEntityOwner());
        return userEntityAdmin;
    }

    public  static RolEntity roleEntityOwner() {
        RolEntity rolEntityWithRoleOwner = new RolEntity();
        rolEntityWithRoleOwner.setIdRol(2L);
        rolEntityWithRoleOwner.setDescription("In charge of the restaurant's dishes and creating employee accounts");
        rolEntityWithRoleOwner.setName("PROPIETARIO");
        return rolEntityWithRoleOwner;
    }

    public  static RolEntity roleEntityEmployee() {
        RolEntity rolEntityWithRoleEmployee = new RolEntity();
        rolEntityWithRoleEmployee.setIdRol(3L);
        rolEntityWithRoleEmployee.setDescription("In charge of taking orders");
        rolEntityWithRoleEmployee.setName("EMPLEADO");
        return rolEntityWithRoleEmployee;
    }

    public  static RolEntity roleEntityCustomer() {
        RolEntity rolEntityWithRoleCustomer = new RolEntity();
        rolEntityWithRoleCustomer.setIdRol(4L);
        rolEntityWithRoleCustomer.setDescription("Food court customer");
        rolEntityWithRoleCustomer.setName("CLIENTE");
        return rolEntityWithRoleCustomer;
    }

    public static CustomerRequestDto customerRequestDtoWithIdRoleOne() {
        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("testName");
        customerRequestDto.setLastName("testLastName");
        customerRequestDto.setCellPhone("3114576972");
        customerRequestDto.setIdentificationDocument(8103743235L);
        customerRequestDto.setEmail("test@test.com");
        customerRequestDto.setPassword("123");
        customerRequestDto.setIdRol(1L);
        return customerRequestDto;
    }

    public static UserRequestToCreateEmployeeDto objectUserEmployee() {
        UserRequestToCreateEmployeeDto userEmployee = new UserRequestToCreateEmployeeDto();
        userEmployee.setName("employeeName");
        userEmployee.setLastName("testLastName");
        userEmployee.setCellPhone("3114576972");
        userEmployee.setIdentificationDocument(8103743235L);
        userEmployee.setEmail("employee@employee.com");
        userEmployee.setPassword("123");
        userEmployee.setIdRol(3L);
        return userEmployee;
    }

    public static CustomerRequestDto customerUserRequest() {
        CustomerRequestDto customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setName("testName");
        customerRequestDto.setLastName("testLastName");
        customerRequestDto.setCellPhone("3114576972");
        customerRequestDto.setIdentificationDocument(8103743235L);
        customerRequestDto.setEmail("test@test.com");
        customerRequestDto.setPassword("123");
        customerRequestDto.setIdRol(4L);
        return customerRequestDto;
    }

    public static UserRequestToCreateEmployeeDto objectUserOwnerRequest() {
        UserRequestToCreateEmployeeDto userOwner = new UserRequestToCreateEmployeeDto();
        userOwner.setName("Owner");
        userOwner.setLastName("LastName");
        userOwner.setCellPhone("3125678902");
        userOwner.setIdentificationDocument(353534292L);
        userOwner.setEmail("new-owner@owner.com");
        userOwner.setPassword("123");
        userOwner.setIdRol(2L);
        return userOwner;
    }

}
