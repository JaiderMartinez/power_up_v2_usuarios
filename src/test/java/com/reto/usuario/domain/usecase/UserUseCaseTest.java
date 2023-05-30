package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.gateways.IEmployeeRestaurantClientSmallSquare;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    private IRolPersistenceDomainPort rolPersistenceDomainPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IEmployeeRestaurantClientSmallSquare employeeRestaurantClientSmallSquare;

    private static final String TOKEN_WITH_BEARER_PREFIX = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvd25lckBvd25lci5jb20iLCJpYXQiOjE2ODUyMjQ2NTUsImV4cCI6MTY4NzgxNjY1NCwibGFzdE5hbWUiOiJmb3Jlcm8iLCJuYW1lIjoiam9oYW5hIiwicm9sIjpbIlJPTEVfUFJPUElFVEFSSU8iXX0.TbOPlE9wsW_HRlPfGnCZ8014cyTHHEvlTd38sa7P3tc";

    @Test
    void test_registerUserWithOwnerRole_withObjectAsUserModelWithoutFieldRole_shouldReturnUserModelWherePasswordThisEncryptedAndRoleOwner() {
        //Given
        RolModel rolExpected = new RolModel();
        rolExpected.setName("PROPIETARIO");
        UserModel userExpected = new UserModel(1L, "Jose", "Martinez", 7388348534L,
                "+574053986322", "test-owner@owner.com", passwordEncoder.encode("123"), rolExpected);
        userExpected.setIdUser(1L);
        UserModel userActual = new UserModel();
        userActual.setName("Jose");
        userActual.setLastName("Martinez");
        userActual.setIdentificationDocument(7388348534L);
        userActual.setCellPhone("+574053986322");
        userActual.setEmail("test-owner@owner.com");
        userActual.setPassword("123");
        when(rolPersistenceDomainPort.findByName("PROPIETARIO")).thenReturn(rolExpected);
        when(userPersistenceDomainPort.saveUser(userActual)).thenReturn(userExpected);
        //When
        UserModel resulWhenSavingUser = userUseCase.registerUserWithOwnerRole(userActual);
        //Then
        verify(userPersistenceDomainPort).saveUser(userActual);
        assertEquals(userExpected.getIdUser(), resulWhenSavingUser.getIdUser());
        assertEquals(userExpected.getName(), resulWhenSavingUser.getName());
        assertEquals(userExpected.getLastName(), resulWhenSavingUser.getLastName());
        assertEquals(userExpected.getIdentificationDocument(), resulWhenSavingUser.getIdentificationDocument());
        assertEquals(userExpected.getCellPhone(), resulWhenSavingUser.getCellPhone());
        assertEquals(userExpected.getEmail(), resulWhenSavingUser.getEmail());
        assertEquals(userExpected.getPassword(), resulWhenSavingUser.getPassword());
        assertEquals(userExpected.getRol(), resulWhenSavingUser.getRol());
        assertEquals(userExpected.getRol().getIdRol(), resulWhenSavingUser.getRol().getIdRol());
        assertEquals(userExpected.getRol().getName(), resulWhenSavingUser.getRol().getName());
    }

    @Test
    void test_registerUserWithOwnerRole_withUserModelFieldsEmpty_shouldThrowEmptyFieldsException() {
        //Given
        UserModel userModelWithFieldsEmpty = new UserModel();
        userModelWithFieldsEmpty.setName("");
        userModelWithFieldsEmpty.setLastName("");
        userModelWithFieldsEmpty.setPassword("");
        userModelWithFieldsEmpty.setCellPhone("");
        userModelWithFieldsEmpty.setIdentificationDocument(null);
        userModelWithFieldsEmpty.setEmail("test-owner@example.co");
        //When & Then
        Assertions.assertThrows(
                EmptyFieldsException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithFieldsEmpty)
        );
    }

    @Test
    void test_registerUserWithOwnerRole_withStringAsEmailInvalid_shouldThrowInvalidEmailFormatException() {
        //Given
        UserModel userModelWithStructureEmailInvalid = new UserModel();
        userModelWithStructureEmailInvalid.setName("Jose");
        userModelWithStructureEmailInvalid.setLastName("Martinez");
        userModelWithStructureEmailInvalid.setIdentificationDocument(7388348534L);
        userModelWithStructureEmailInvalid.setCellPhone("3115996723");
        userModelWithStructureEmailInvalid.setEmail("invalid.email-without-at-sign.com");
        userModelWithStructureEmailInvalid.setPassword("123");
        //When & Then
        Assertions.assertThrows(
                InvalidEmailFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithStructureEmailInvalid)
        );
    }

    @Test
    void test_registerUserWithOwnerRole_withStringAsExistingEmailInTheDataBase_shouldThrowEmailExistsException() {
        //Given
        UserModel userModelWithEmailExists = new UserModel();
        userModelWithEmailExists.setName("Jose");
        userModelWithEmailExists.setLastName("Martinez");
        userModelWithEmailExists.setIdentificationDocument(7388348534L);
        userModelWithEmailExists.setCellPhone("3115996723");
        userModelWithEmailExists.setEmail("email-exists@example.com");
        userModelWithEmailExists.setPassword("123");
        when(userPersistenceDomainPort.existsByEmail(userModelWithEmailExists.getEmail())).thenReturn(true);
        //When & Then
        Assertions.assertThrows(
                EmailExistsException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithEmailExists)
        );
    }

    @Test
    void test_registerUserWithOwnerRole_withStringAsFieldCellPhoneInvalidAndUserModel_shouldThrowInvalidCellPhoneFormatException() {
        //Given
        UserModel userModelWithCellPhoneInvalid = new UserModel();
        userModelWithCellPhoneInvalid.setName("Jose");
        userModelWithCellPhoneInvalid.setLastName("Martinez");
        userModelWithCellPhoneInvalid.setIdentificationDocument(7388348534L);
        userModelWithCellPhoneInvalid.setCellPhone("+5731159967232334");
        userModelWithCellPhoneInvalid.setEmail("email@example.com");
        userModelWithCellPhoneInvalid.setPassword("123");
        //When & Then
        Assertions.assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithCellPhoneInvalid)
        );
    }

    @Test
    void test_registerUserWithEmployeeRole_withAllFieldsValidInObjectAsUserModelAndTokenValid_shouldReturnUserEmployeeSaved() {
        //Given
        final UserModel userOwnerExpected = new UserModel(1L, "Owner", "Santiago", 38774375L, "3113986382", "owner@owner.com",
                "123", new RolModel(2L, "PROPIETARIO", "owner restaurant"));
        final RolModel rolEmployeeExpected = new RolModel(3L, "EMPLEADO", "employee restaurant");
        final UserModel userEmployeeExpected = new UserModel(2L, "Jose", "Martinez", 7388348534L, "+574053986322",
                "test-employee@employee.com", "123", rolEmployeeExpected);
        RolModel rolRequest = new RolModel();
        rolRequest.setIdRol(3L);
        UserModel userEmployeeRequest = new UserModel();
        userEmployeeRequest.setName("Jose");
        userEmployeeRequest.setLastName("Martinez");
        userEmployeeRequest.setIdentificationDocument(7388348534L);
        userEmployeeRequest.setCellPhone("+574053986322");
        userEmployeeRequest.setEmail("test-employee@employee.com");
        userEmployeeRequest.setPassword("123");
        userEmployeeRequest.setRol(rolRequest);

        when(this.rolPersistenceDomainPort.findByIdRol(userEmployeeRequest.getRol().getIdRol())).thenReturn(rolEmployeeExpected);
        when(this.userPersistenceDomainPort.findByEmail(userOwnerExpected.getEmail())).thenReturn(userOwnerExpected);
        when(this.userPersistenceDomainPort.saveUser(userEmployeeRequest)).thenReturn(userEmployeeExpected);
        doNothing().when(this.employeeRestaurantClientSmallSquare).saveUserEmployeeToARestaurant(
                argThat(request -> request.getIdOwnerRestaurant().equals(userOwnerExpected.getIdUser()) &&
                                   request.getEmployeeUserId().equals(userEmployeeExpected.getIdUser())),
                eq(TOKEN_WITH_BEARER_PREFIX));
        //When
        final UserModel resultFromUserEmployeeSaved = this.userUseCase.registerUserWithEmployeeRole(userEmployeeRequest, TOKEN_WITH_BEARER_PREFIX);
        //Then
        verify(this.rolPersistenceDomainPort, times(1)).findByIdRol(userEmployeeRequest.getRol().getIdRol());
        verify(this.userPersistenceDomainPort, times(1)).findByEmail(userOwnerExpected.getEmail());
        verify(this.userPersistenceDomainPort, times(1)).saveUser(userEmployeeRequest);
        assertEquals(userEmployeeExpected.getIdUser(), resultFromUserEmployeeSaved.getIdUser());
        assertEquals(userEmployeeExpected.getName(), resultFromUserEmployeeSaved.getName());
        assertEquals(userEmployeeExpected.getLastName(), resultFromUserEmployeeSaved.getLastName());
        assertEquals(userEmployeeExpected.getIdentificationDocument(), resultFromUserEmployeeSaved.getIdentificationDocument());
        assertEquals(userEmployeeExpected.getCellPhone(), resultFromUserEmployeeSaved.getCellPhone());
        assertEquals(userEmployeeExpected.getEmail(), resultFromUserEmployeeSaved.getEmail());
        assertEquals(userEmployeeExpected.getPassword(), resultFromUserEmployeeSaved.getPassword());
        assertEquals(userEmployeeExpected.getRol().getName(), resultFromUserEmployeeSaved.getRol().getName());
        assertEquals(userEmployeeExpected.getRol().getDescription(), resultFromUserEmployeeSaved.getRol().getDescription());
    }

    @Test
    void test_registerUserWithEmployeeRole_withFieldIdRolIsDifferentToEmployeeAndTokenValid_shouldThrowRolNotFoundException() {
        //Given
        RolModel rolRequest = new RolModel();
        rolRequest.setIdRol(3L);
        UserModel userEmployeeRequest = new UserModel();
        userEmployeeRequest.setName("Jose");
        userEmployeeRequest.setLastName("Martinez");
        userEmployeeRequest.setIdentificationDocument(7388348534L);
        userEmployeeRequest.setCellPhone("+574053986322");
        userEmployeeRequest.setEmail("test-employee@employee.com");
        userEmployeeRequest.setPassword("123");
        userEmployeeRequest.setRol(rolRequest);
        //When & Then
        Assertions.assertThrows(
                RolNotFoundException.class,
                () -> userUseCase.registerUserWithEmployeeRole(userEmployeeRequest, TOKEN_WITH_BEARER_PREFIX)
        );
    }
}