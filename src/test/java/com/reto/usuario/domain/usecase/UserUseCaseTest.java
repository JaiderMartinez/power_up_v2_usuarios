package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.exceptions.UserNotFoundException;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.exceptions.EmailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    UserUseCase userUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    IRolPersistenceDomainPort rolPersistenceDomainPort;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userUseCase = new UserUseCase(userPersistenceDomainPort, rolPersistenceDomainPort, passwordEncoder);
    }

    @Test
    void test_registerUserWithOwnerRole_withObjectAsUserModel_whenSystemCreateAnOwnerAccount_ShouldReturnVoid() {
        RolModel rolOwnerExpected = new RolModel();
        rolOwnerExpected.setIdRol(2L);
        rolOwnerExpected.setName("PROPIETARIO");

        UserModel userExpected = new UserModel();
        userExpected.setName("Luis");
        userExpected.setLastName("Martinez");
        userExpected.setPassword("12345678");
        userExpected.setCellPhone("+574053986322");
        userExpected.setIdentificationDocument(7388348534L);
        userExpected.setEmail("test-exception@gmail.com");

        when(rolPersistenceDomainPort.findByName(rolOwnerExpected.getName())).thenReturn(rolOwnerExpected);
        when(userPersistenceDomainPort.saveUser(userExpected)).thenReturn(userExpected);
        userUseCase.registerUserWithOwnerRole(userExpected);
        verify(userPersistenceDomainPort, times(1)).saveUser(userExpected);
    }

    @Test
    void mustRegisterUserWithEmployeeRole() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(3L);
        rolModel.setName("EMPLEADO");
        rolModel.setDescription("");
        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(FactoryUserModelTest.rolModelEmployee());
        when(userPersistenceDomainPort.saveUser(any(UserModel.class))).thenReturn(FactoryUserModelTest.userModelWithEmployeeRole());
        userUseCase.registerUserWithEmployeeRole(FactoryUserModelTest.userModelWithEmployeeRole());
        verify(userPersistenceDomainPort, times(1)).saveUser(any(UserModel.class));
    }

    @Test
    void test_findByEmail_withUsernameFromUser_whenSystemFindUserByEmail_ShouldReturnAnUser() {
        UserModel user = new UserModel();
        user.setName("Luis");
        user.setLastName("Martinez");
        user.setPassword("12345678");
        user.setCellPhone("+574053986322");
        user.setIdentificationDocument(7388348534L);
        user.setEmail("test-email@gmail.com");
        user.setRol(new RolModel("PROPIETARIO", ""));

        when(userPersistenceDomainPort.findByEmail("test-email@gmail.com")).thenReturn(user);
        UserModel result = userUseCase.findUserByEmail("test-email@gmail.com");
        verify(userPersistenceDomainPort).findByEmail(result.getEmail());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals(user.getRol().getName(), result.getRol().getName());
        Assertions.assertEquals(user.getCellPhone(), result.getCellPhone());
    }

    @Test
    void test_validateUserFieldsEmpty_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmptyFieldsException() {
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

        assertThrows(
                EmptyFieldsException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModel)
        );
    }

    @Test
    void test_validateEmailStructure_withStringAsEmail_whenSystemRegisterUser_ShouldThrowInvalidEmailFormatException() {
        UserModel userModelEmailStructureInvalid = new UserModel();
        userModelEmailStructureInvalid.setName("Luis");
        userModelEmailStructureInvalid.setLastName("Martinez");
        userModelEmailStructureInvalid.setPassword("12345678");
        userModelEmailStructureInvalid.setCellPhone("3013986322");
        userModelEmailStructureInvalid.setIdentificationDocument(7388348534L);
        userModelEmailStructureInvalid.setEmail("test-exception-invalid.structuregmail.com.");
        userModelEmailStructureInvalid.setRol(new RolModel("PROPIETARIO", ""));

        assertThrows(
                InvalidEmailFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelEmailStructureInvalid)
        );
    }

    @Test
    void test_restrictionsWhenSavingAUser_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmailExistsException() {
        UserModel userOwner = new UserModel();
        userOwner.setName("Luis");
        userOwner.setLastName("Martinez");
        userOwner.setPassword("12345678");
        userOwner.setCellPhone("+574053986322");
        userOwner.setIdentificationDocument(7388348534L);
        userOwner.setEmail("test@example.com");

        when(userPersistenceDomainPort.existsByEmail(userOwner.getEmail())).thenReturn(true);
        assertThrows( EmailExistsException.class, () -> userUseCase.registerUserWithOwnerRole(userOwner));
    }

    @Test
    void test_findUserByEmail_withObjectUserModel_whenSystemRegisterUser_ShouldThrowEmailNotFoundException() {
        assertThrows(
                EmailNotFoundException.class,
                () -> userUseCase.findUserByEmail("email-not-exist@gmail.com")
        );
    }

    @Test
    void test_validateUserFieldsEmpty_withObjectUserModel_whenSystemRegisterUser_ShouldThrowInvalidCellPhoneFormatException() {
        UserModel userWithCellPhoneInvalid = new UserModel();
        userWithCellPhoneInvalid.setName("Luis");
        userWithCellPhoneInvalid.setLastName("Martinez");
        userWithCellPhoneInvalid.setPassword("12345678");
        userWithCellPhoneInvalid.setCellPhone("+43301398632223");
        userWithCellPhoneInvalid.setIdentificationDocument(7388348534L);
        userWithCellPhoneInvalid.setEmail("test-invalidcellphoneformat@exception.com");

        assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userWithCellPhoneInvalid)
        );
    }

    @Test
    void test_findRoleByIdAndCompareRoleName_withStringAsRoleNameAndStringAsIdRol_whenSystemRegisterUser_ShouldThrowRolNotFoundException() {
        RolModel rolModel = new RolModel();
        rolModel.setIdRol(3L);
        UserModel userWithEmployeeRoleThatDoesNotExist = new UserModel();
        userWithEmployeeRoleThatDoesNotExist.setName("Luis");
        userWithEmployeeRoleThatDoesNotExist.setLastName("Martinez");
        userWithEmployeeRoleThatDoesNotExist.setPassword("12345678");
        userWithEmployeeRoleThatDoesNotExist.setCellPhone("+574053986322");
        userWithEmployeeRoleThatDoesNotExist.setIdentificationDocument(7388348534L);
        userWithEmployeeRoleThatDoesNotExist.setEmail("test-exception@gmail.com");
        userWithEmployeeRoleThatDoesNotExist.setRol(rolModel);

        when(rolPersistenceDomainPort.findByIdRol(3L)).thenReturn(null);
        assertThrows(
                RolNotFoundException.class,
                () -> userUseCase.registerUserWithEmployeeRole(userWithEmployeeRoleThatDoesNotExist)
        );
    }

    @Test
    void test_getUserById_withLongAsIdUser_whenSystemFindUserById_ShouldReturnAnUser() {
        UserModel userExpected = new UserModel();
        userExpected.setIdUser(1L);
        userExpected.setName("Luis");
        userExpected.setLastName("Martinez");
        userExpected.setPassword("12345678");
        userExpected.setCellPhone("+574053986322");
        userExpected.setIdentificationDocument(7388348534L);
        userExpected.setEmail("test@example.com");
        userExpected.setRol(new RolModel("ADMINISTRADOR", ""));

        when(userPersistenceDomainPort.findById(1L)).thenReturn(userExpected);

        UserModel resul = userUseCase.getUserById(1L);
        verify(userPersistenceDomainPort).findById(1L);
        Assertions.assertEquals(userExpected.getIdUser(), resul.getIdUser());
        Assertions.assertEquals(userExpected.getEmail(), resul.getEmail());
        Assertions.assertEquals(userExpected.getRol().getName(), resul.getRol().getName());
        Assertions.assertEquals(userExpected.getIdentificationDocument(), resul.getIdentificationDocument());
    }

    @Test
    void test_getUserById_withLongAsIdUser_whenSystemFindUserById_ShouldThrowUserNotFoundException() {
        when(userPersistenceDomainPort.findById(1L)).thenReturn(null);
        assertThrows(
                UserNotFoundException.class,
                () -> userUseCase.getUserById(1L)
        );
    }
}