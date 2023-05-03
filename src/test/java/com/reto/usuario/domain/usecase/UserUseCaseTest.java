package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {

    @InjectMocks
    UserUseCase userUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    IRolPersistenceDomainPort rolPersistenceDomainPort;


    @Test
    void mustRegisterUserWithOwnerRole() {
        when(rolPersistenceDomainPort.findByName("PROPIETARIO")).thenReturn(FactoryUserModelTest.rolModel());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithOwnerRole());
        userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel());
        verify(userPersistenceDomainPort).saveUser(any(UserModel.class));
    }

    @Test
    void mustRegisterUserWithEmployeeRole() {
        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(FactoryUserModelTest.rolModelEmployee());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithEmployeeRole());
        userUseCase.registerUserWithEmployeeRole(FactoryUserModelTest.userModelWithEmployeeRole());
        verify(userPersistenceDomainPort, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void mustRegisterUserWithCustomerRole() {
        when(rolPersistenceDomainPort.findByIdRol(4L)).thenReturn(FactoryUserModelTest.rolModelCustomer());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithCustomerRole());
        userUseCase.registerUserWithCustomerRole(FactoryUserModelTest.userModelWithCustomerRole());
        verify(userPersistenceDomainPort, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void findUsuarioByCorreo() {
        when(userPersistenceDomainPort.findByEmail("test-email@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        UserModel userModel = userUseCase.findUserByEmail("test-email@gmail.com");
        verify(userPersistenceDomainPort).findByEmail(any(String.class));
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userModel.getEmail());
    }

    @Test
    void throwRolNotFoundExceptionWhenRegisterUser() {
        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(FactoryUserModelTest.rolModelEmployee());

        UserModel userModel = FactoryUserModelTest.userModelWithEmployeeRoleThatDoesNotExist();

        assertThrows(
                RolNotFoundException.class,
                () -> userUseCase.registerUserWithEmployeeRole(
                        FactoryUserModelTest.userModelWithEmployeeRoleThatDoesNotExist())
        );
    }

    @Test
    void throwEmptyFieldsExceptionWhenRegisterUser() {
        UserModel userModelWithFieldsEmpty =
                FactoryUserModelTest.userModelFieldsEmpty();
        assertThrows(
                EmptyFieldsException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithFieldsEmpty)
        );
    }

    @Test
    void throwEmailStructureInvalidExceptionWhenRegisterUser() {
        UserModel userModelEmailStructureInvalid =
                FactoryUserModelTest.userModelEmailStructureInvalid();
        assertThrows(
                InvalidEmailFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelEmailStructureInvalid)
        );
    }

    @Test
    void throwEmailExistsExceptionWhenRegisterUser() {
        when(userPersistenceDomainPort.existsByEmail(FactoryUserModelTest.userModel().getEmail())).thenReturn(true);
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModel());
        assertThrows(
                EmailExistsException.class,
                () -> userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel())
        );
    }

    @Test
    void throwEmailNotFoundExceptionWhenFindEmail() {
        when(userPersistenceDomainPort.findByEmail("test-exception@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        assertThrows(
                EmailNotFoundException.class,
                () -> userUseCase.findUserByEmail("email-not-exist@gmail.com")
        );
    }

    @Test
    void throwInvalidCellPhoneFormatExceptionWhenRegisterUser() {
        UserModel userModelCellPhoneInvalid =
                FactoryUserModelTest.userModelCellPhoneInvalid();
        assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelCellPhoneInvalid)
        );
    }
}