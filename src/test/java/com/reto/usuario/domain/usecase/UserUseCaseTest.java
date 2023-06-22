package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.exceptions.RolNotFoundException;
import com.reto.usuario.domain.spi.clients.IEmployeeRestaurantClientSmallSquare;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.persistence.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.persistence.IUserPersistenceDomainPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private static final String TOKEN_WITH_BEARER_PREFIX = "Bearer + token";

    @Test
    void test_registerUserWithOwnerRole_withObjectAsUserModelWithoutFieldRole_shouldReturnUserModelWherePasswordThisEncryptedAndRoleOwner() {
        //Given
        RolModel rolExpected = new RolModel();
        rolExpected.setName("PROPIETARIO");
        UserModel userExpected = new UserModel(1L, "Jose", "Martinez", 7388348534L,
                "+574053986322", "test-owner@owner.com", passwordEncoder.encode("123"), rolExpected);

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
        assertThrows(
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
        assertThrows(
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
        assertThrows(
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
        assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> userUseCase.registerUserWithOwnerRole(userModelWithCellPhoneInvalid)
        );
    }

    @Test
    void test_registerUserWithEmployeeRole_withAllFieldsValidInObjectAsUserModelAndTokenValid_shouldReturnUserEmployeeSaved() {
        //Given
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
        Long idRestaurant = 15L;

        when(this.rolPersistenceDomainPort.findByIdRol(userEmployeeRequest.getRol().getIdRol())).thenReturn(rolEmployeeExpected);
        when(this.userPersistenceDomainPort.saveUser(userEmployeeRequest)).thenReturn(userEmployeeExpected);
        doNothing().when(this.employeeRestaurantClientSmallSquare).saveUserEmployeeToARestaurant(
                argThat(request -> request.getIdUserEmployee().equals(userEmployeeExpected.getIdUser()) &&
                                   request.getIdRestaurant().equals(idRestaurant)),
                eq(TOKEN_WITH_BEARER_PREFIX));
        //When
        final UserModel resultFromUserEmployeeSaved = this.userUseCase.registerUserWithEmployeeRole(userEmployeeRequest, TOKEN_WITH_BEARER_PREFIX, idRestaurant);
        //Then
        verify(this.rolPersistenceDomainPort, times(1)).findByIdRol(userEmployeeRequest.getRol().getIdRol());
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
        assertThrows(
                RolNotFoundException.class,
                () -> userUseCase.registerUserWithEmployeeRole(userEmployeeRequest, TOKEN_WITH_BEARER_PREFIX, 15L)
        );
    }

    @Test
    void test_registerUserWithCustomerRole_withAllFieldsValidFromObjectAsUserModel_shouldReturnUserModelWherePasswordThisEncryptedAndRoleCustomer() {
        //Given
        RolModel rolExpected = new RolModel(4L, "CLIENTE", "");
        UserModel userExpected = new UserModel(1L, "Jose", "Martinez", 7388348534L,
                "+574053986322", "test-customer@customer.com", "123", rolExpected);

        RolModel rolRequest = new RolModel();
        rolRequest.setIdRol(4L);
        UserModel customerRequest = new UserModel();
        customerRequest.setName("Jose");
        customerRequest.setLastName("Martinez");
        customerRequest.setIdentificationDocument(7388348534L);
        customerRequest.setCellPhone("+574053986322");
        customerRequest.setEmail("test-customer@customer.com");
        customerRequest.setPassword("123");
        customerRequest.setRol(rolRequest);
        when(this.rolPersistenceDomainPort.findByIdRol(4L)).thenReturn(rolExpected);
        when(this.userPersistenceDomainPort.saveUser(customerRequest)).thenReturn(userExpected);
        //When
        UserModel resulWhenSavingUserCustomer = this.userUseCase.registerUserWithCustomerRole(customerRequest);
        //Then
        verify(this.userPersistenceDomainPort).saveUser(customerRequest);
        verify(this.rolPersistenceDomainPort).findByIdRol(4L);
        assertEquals(userExpected.getIdUser(), resulWhenSavingUserCustomer.getIdUser());
        assertEquals(userExpected.getName(), resulWhenSavingUserCustomer.getName());
        assertEquals(userExpected.getLastName(), resulWhenSavingUserCustomer.getLastName());
        assertEquals(userExpected.getIdentificationDocument(), resulWhenSavingUserCustomer.getIdentificationDocument());
        assertEquals(userExpected.getCellPhone(), resulWhenSavingUserCustomer.getCellPhone());
        assertEquals(userExpected.getEmail(), resulWhenSavingUserCustomer.getEmail());
        assertEquals(userExpected.getPassword(), resulWhenSavingUserCustomer.getPassword());
        assertEquals(userExpected.getRol(), resulWhenSavingUserCustomer.getRol());
        assertEquals(userExpected.getRol().getIdRol(), resulWhenSavingUserCustomer.getRol().getIdRol());
        assertEquals(userExpected.getRol().getName(), resulWhenSavingUserCustomer.getRol().getName());
    }

    @Test
    void test_registerUserWithCustomerRole_withUserModelAllFieldsEmptyExceptTheEmail_shouldThrowEmptyFieldsException() {
        //Given
        UserModel userModelWithFieldsEmpty = new UserModel();
        userModelWithFieldsEmpty.setName("");
        userModelWithFieldsEmpty.setLastName("");
        userModelWithFieldsEmpty.setPassword("");
        userModelWithFieldsEmpty.setCellPhone("");
        userModelWithFieldsEmpty.setIdentificationDocument(null);
        userModelWithFieldsEmpty.setEmail("test-owner@example.co");
        //When
        EmptyFieldsException messageException = assertThrows( EmptyFieldsException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithFieldsEmpty) );
        //Then
        assertEquals("Fields cannot be empty", messageException.getMessage());
    }

    @Test
    void test_registerUserWithCustomerRole_withStringAsEmailInvalidFromObjectUserModel_shouldThrowInvalidEmailFormatException() {
        //Given
        UserModel userModelWithStructureEmailInvalid = new UserModel();
        userModelWithStructureEmailInvalid.setName("Jose");
        userModelWithStructureEmailInvalid.setLastName("Martinez");
        userModelWithStructureEmailInvalid.setIdentificationDocument(7388348534L);
        userModelWithStructureEmailInvalid.setCellPhone("3115996723");
        userModelWithStructureEmailInvalid.setEmail("invalid.email-without-at-sign.com");
        userModelWithStructureEmailInvalid.setPassword("123");
        //When
        InvalidEmailFormatException messageException = assertThrows(InvalidEmailFormatException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithStructureEmailInvalid));
        //Then
        assertEquals("Wrong email structure", messageException.getMessage());
    }

    @Test
    void test_registerUserWithCustomerRole_withFieldEmailExistingInTheDataBase_shouldThrowEmailExistsException() {
        //Given
        UserModel userModelWithEmailExists = new UserModel();
        userModelWithEmailExists.setName("Jose");
        userModelWithEmailExists.setLastName("Martinez");
        userModelWithEmailExists.setIdentificationDocument(7388348534L);
        userModelWithEmailExists.setCellPhone("3115996723");
        userModelWithEmailExists.setEmail("email-exists@example.com");
        userModelWithEmailExists.setPassword("123");
        when(userPersistenceDomainPort.existsByEmail(userModelWithEmailExists.getEmail())).thenReturn(true);
        //When
        EmailExistsException messageException = assertThrows( EmailExistsException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithEmailExists));
        //Then
        assertEquals("The email " + userModelWithEmailExists.getEmail()  + " already exists", messageException.getMessage());
    }

    @Test
    void test_registerUserWithCustomerRole_withStringAsFieldCellPhoneInvalidFromObjectAsUserModel_shouldThrowInvalidCellPhoneFormatException() {
        //Given
        UserModel userModelWithCellPhoneInvalid = new UserModel();
        userModelWithCellPhoneInvalid.setName("Jose");
        userModelWithCellPhoneInvalid.setLastName("Martinez");
        userModelWithCellPhoneInvalid.setIdentificationDocument(7388348534L);
        userModelWithCellPhoneInvalid.setCellPhone("+5731159967232334");
        userModelWithCellPhoneInvalid.setEmail("email@example.com");
        userModelWithCellPhoneInvalid.setPassword("123");
        //When
        InvalidCellPhoneFormatException messageException = assertThrows( InvalidCellPhoneFormatException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithCellPhoneInvalid) );
        //Then
        assertEquals("The cell phone format is wrong", messageException.getMessage());
    }

    @Test
    void test_registerUserWithCustomerRole_withObjectAsUserModelWhereFieldIdRoleNotFoundInTheDataBase_shouldThrowRolNotFoundException() {
        //Given
        RolModel rolModelRequest = new RolModel();
        rolModelRequest.setIdRol(1000L);
        UserModel userModelWithCellPhoneInvalid = new UserModel();
        userModelWithCellPhoneInvalid.setName("Jose");
        userModelWithCellPhoneInvalid.setLastName("Martinez");
        userModelWithCellPhoneInvalid.setIdentificationDocument(7388348534L);
        userModelWithCellPhoneInvalid.setCellPhone("+575042019851");
        userModelWithCellPhoneInvalid.setEmail("email@example.com");
        userModelWithCellPhoneInvalid.setPassword("123");
        userModelWithCellPhoneInvalid.setRol(rolModelRequest);
        //When
        RolNotFoundException messageException = assertThrows( RolNotFoundException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithCellPhoneInvalid) );
        //Then
        assertEquals("The rol not found", messageException.getMessage());
    }

    @Test
    void test_registerUserWithCustomerRole_withFieldIdRoleIsDifferentTheValueFromNameFromRoleCustomerFoundOfTheDataBase_shouldThrowRolNotFoundException() {
        //Given
        RolModel rolModelExpected = new RolModel();
        rolModelExpected.setIdRol(4L);
        rolModelExpected.setName("EMPLEADO");

        RolModel rolModelRequest = new RolModel();
        rolModelRequest.setIdRol(4L);
        UserModel userModelWithCellPhoneInvalid = new UserModel();
        userModelWithCellPhoneInvalid.setName("Jose");
        userModelWithCellPhoneInvalid.setLastName("Martinez");
        userModelWithCellPhoneInvalid.setIdentificationDocument(7388348534L);
        userModelWithCellPhoneInvalid.setCellPhone("+575042019851");
        userModelWithCellPhoneInvalid.setEmail("email@example.com");
        userModelWithCellPhoneInvalid.setPassword("123");
        userModelWithCellPhoneInvalid.setRol(rolModelRequest);
        when(this.rolPersistenceDomainPort.findByIdRol(4L)).thenReturn(rolModelExpected);
        //When
        RolNotFoundException messageException = assertThrows( RolNotFoundException.class, () -> userUseCase.registerUserWithCustomerRole(userModelWithCellPhoneInvalid) );
        //Then
        assertEquals("The rol is different to the requested", messageException.getMessage());
    }
}