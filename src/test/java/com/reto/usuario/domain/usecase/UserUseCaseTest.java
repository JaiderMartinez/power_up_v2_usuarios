package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.exceptions.EmptyFieldsException;
import com.reto.usuario.domain.exceptions.InvalidCellPhoneFormatException;
import com.reto.usuario.domain.exceptions.InvalidEmailFormatException;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
    void registerUserWithOwnerRole() {
        when(rolPersistenceDomainPort.findByNombre("PROPIETARIO")).thenReturn(FactoryUserModelTest.rolModel());
        when(userPersistenceDomainPort.saveUser(any())).thenReturn(FactoryUserModelTest.userModelWithoutRole());
        userUseCase.registerUserWithOwnerRole(FactoryUserModelTest.userModel());
        verify(userPersistenceDomainPort).saveUser(any(UserModel.class));
    }

    @Test
    void signInUseCase() {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setEmail("test@gmail.com");
        authCredentials.setPassword("12345678");
        when(userPersistenceDomainPort.findByEmail(authCredentials.getEmail())).thenReturn(
                FactoryUserModelTest.userModel());
        String token = userUseCase.signInUseCase(authCredentials);
        assertNotNull(token);
        UsernamePasswordAuthenticationToken userAutheticated = TokenUtils.getAuthentication(token);
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userAutheticated.getName());
        verify(userPersistenceDomainPort).findByEmail(authCredentials.getEmail());
    }

    @Test
    void findUsuarioByCorreo() {
        when(userPersistenceDomainPort.findByEmail("test-email@gmail.com")).thenReturn(FactoryUserModelTest.userModel());
        UserModel userModel = userUseCase.findUsuarioByEmail("test-email@gmail.com");
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userModel.getEmail());
        verify(userPersistenceDomainPort).findByEmail(any(String.class));
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> { userUseCase.findUsuarioByEmail("test-email-not-found@gmail.com"); }
        );
    }

    @Test
    void throwEmptyFieldsExceptionWhenRegisterUserWithOwnerRole() {
        UserModel userModelWithFieldsEmpty =
                FactoryUserModelTest.userModelFieldsEmpty();
        Assertions.assertThrows(
                EmptyFieldsException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelWithFieldsEmpty); }
        );
    }

    @Test
    void throwEmailStructureInvalidExceptionWhenRegisterUserWithOwnerRole() {
        UserModel userModelEmailStructureInvalid =
                FactoryUserModelTest.userModelEmailStructureInvalid();
        Assertions.assertThrows(
                InvalidEmailFormatException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelEmailStructureInvalid); }
        );
    }

    @Test
    void throwInvalidCellPhoneFormatExceptionWhenRegisterUserWithOwnerRole() {
        UserModel userModelCellPhoneInvalid =
                FactoryUserModelTest.userModelCellPhoneInvalid();
        Assertions.assertThrows(
                InvalidCellPhoneFormatException.class,
                () -> { userUseCase.registerUserWithOwnerRole(userModelCellPhoneInvalid); }
        );
    }
}