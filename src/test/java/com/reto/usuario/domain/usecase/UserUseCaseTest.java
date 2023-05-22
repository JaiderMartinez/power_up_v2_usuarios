package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.exceptions.EmailExistsException;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
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

    @Test
    void test_registerUserWithOwnerRole_withObjectAsUserModelWithoutFieldRole_shouldReturnUserModelWherePasswordThisEncryptedAndRoleOwner() {
        //Given
        RolModel rolExpected = new RolModel();
        rolExpected.setName("PROPIETARIO");
        UserModel userExpected = new UserModel("Jose", "Martinez", 7388348534L,
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
}