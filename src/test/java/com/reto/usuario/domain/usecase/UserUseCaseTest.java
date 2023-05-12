package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.dto.EmployeeRestaurantClientRequestDto;
import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.exceptions.UserNotFoundException;
import com.reto.usuario.domain.gateways.IEmployeeRestaurantClientPlazoleta;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserUseCaseTest {

    UserUseCase userUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    IRolPersistenceDomainPort rolPersistenceDomainPort;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    IEmployeeRestaurantClientPlazoleta employeeRestaurantClientPlazoleta;

    private final static String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LWVtcGxveWVlQGV4YW1wbGUuY29tIiwiaWF0IjoxNjgzODUxNTU2LCJleHAiOjE2ODY0NDM1NTYsImxhc3ROYW1lIjoic2RmIiwibmFtZSI6ImoiLCJyb2wiOlsiUk9MRV9FTVBMRUFETyJdfQ.Qv_taJuXcIa6Dt_xqqzQ8F1JAUfmuyeD4-uFWD-XT9c";

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userPersistenceDomainPort, rolPersistenceDomainPort, passwordEncoder, employeeRestaurantClientPlazoleta);
    }

    @Test
    void test_registerUserWithOwnerRole_withObjectAsUserOwner_whenSystemCreateAnOwnerAccount_ShouldReturnVoid() {
        when(rolPersistenceDomainPort.findByName("PROPIETARIO")).thenReturn(FactoryUserModelTest.rolModel());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithOwnerRole());
        userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel());
        verify(userPersistenceDomainPort).saveUser(any(UserModel.class));
    }

    @Test
    void test_registerUserWithEmployeeRole_withObjectAsUserOwnerAndTokenWithBearerPrefix_whenSystemCreateAnOwnerAccount_ShouldReturnVoid() {
        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(FactoryUserModelTest.rolModelEmployee());
        when(userPersistenceDomainPort.findByEmail(FactoryUserModelTest.userModelWithEmployeeRole().getEmail()))
                                                    .thenReturn(FactoryUserModelTest.userModelWithEmployeeRole());

        when(userPersistenceDomainPort.saveUser(any(UserModel.class))).thenReturn(FactoryUserModelTest.userModelWithEmployeeRole());
        doNothing().when(employeeRestaurantClientPlazoleta).saveUserEmployeeToARestaurant(any(EmployeeRestaurantClientRequestDto.class), any());
        userUseCase.registerUserWithEmployeeRole(FactoryUserModelTest.userModelWithEmployeeRole(), token);
        verify(userPersistenceDomainPort, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void test_registerUserWithCustomerRole_withObjectAsUserOwner_whenSystemCreateAnOwnerAccount_ShouldReturnVoid() {
        when(rolPersistenceDomainPort.findByIdRol(4L)).thenReturn(FactoryUserModelTest.rolModelCustomer());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithCustomerRole());
        userUseCase.registerUserWithCustomerRole(FactoryUserModelTest.userModelWithCustomerRole());
        verify(userPersistenceDomainPort, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void test_findByEmail_withUsernameFromUser_whenSystemFindUserByEmail_ShouldReturnAnUser() {
        when(userPersistenceDomainPort.findByEmail("test-email@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        UserModel userModel = userUseCase.findUserByEmail("test-email@gmail.com");
        verify(userPersistenceDomainPort).findByEmail(any(String.class));
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userModel.getEmail());
    }

    @Test
    void test_validateUserFieldsEmpty_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmptyFieldsException() {
        UserModel userModelWithFieldsEmpty =
                FactoryUserModelTest.userModelFieldsEmpty();
        assertThrows(
                EmptyFieldsException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithFieldsEmpty)
        );
    }

    @Test
    void test_validateEmailStructure_withStringAsEmail_whenSystemRegisterUser_ShouldThrowInvalidEmailFormatException() {
        UserModel userModelEmailStructureInvalid =
                FactoryUserModelTest.userModelEmailStructureInvalid();
        assertThrows(
                InvalidEmailFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelEmailStructureInvalid)
        );
    }

    @Test
    void test_restrictionsWhenSavingAUser_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmailExistsException() {
        when(userPersistenceDomainPort.existsByEmail(FactoryUserModelTest.userModel().getEmail())).thenReturn(true);
        assertThrows( EmailExistsException.class, () -> userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel()));
    }

    @Test
    void test_findUserByEmail_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmailNotFoundException() {
        when(userPersistenceDomainPort.findByEmail("test-exception@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        assertThrows(
                EmailNotFoundException.class,
                () -> userUseCase.findUserByEmail("email-not-exist@gmail.com")
        );
    }

    @Test
    void test_validateUserFieldsEmpty_withObjectUserModel_whenSystemRegisterUser_ShouldThrowInvalidCellPhoneFormatException() {
        UserModel userModelCellPhoneInvalid =
                FactoryUserModelTest.userModelCellPhoneInvalid();
        assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelCellPhoneInvalid)
        );
    }

    @Test
    void test_findRoleByIdAndCompareRoleName_withStringAsRoleNameAndStringAsIdRol_whenSystemRegisterUser_ShouldThrowRolNotFoundException() {
        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(FactoryUserModelTest.rolModelCustomer());
        assertThrows(
                RolNotFoundException.class,
                () -> userUseCase.registerUserWithCustomerRole(
                        FactoryUserModelTest.userModelWithEmployeeRoleThatDoesNotExist())
        );
    }

    @Test
    void test_getUserById_withLongAsIdUser_whenSystemFindUserById_ShouldReturnAnUser() {
        when(userPersistenceDomainPort.findById(1L)).thenReturn(FactoryUserModelTest.userModel());
        UserModel userModel = userUseCase.getUserById(1L);
        verify(userPersistenceDomainPort).findById(1L);
        Assertions.assertEquals(FactoryUserModelTest.userModel().getIdUser(), userModel.getIdUser());
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userModel.getEmail());
        Assertions.assertEquals(FactoryUserModelTest.userModel().getRol().getIdRol(), userModel.getRol().getIdRol());
        Assertions.assertEquals(FactoryUserModelTest.userModel().getIdentificationDocument(), userModel.getIdentificationDocument());
    }

    @Test
    void test_test_getUserById_withLongAsIdUser_whenSystemFindUserById_ShouldThrowUserNotFoundException() {
        when(userPersistenceDomainPort.findById(3L)).thenReturn(FactoryUserModelTest.userModel());
        assertThrows(
                UserNotFoundException.class,
                () -> userUseCase.getUserById(1L)
        );
    }

}