package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    AuthUseCase authUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @BeforeEach
    void setup() {
        authUseCase = new AuthUseCase(userPersistenceDomainPort);
    }

    @Test
    void test_signInUseCase_withAuthCredentials_whenSystemAuthenticatesTheUser_ShouldAToken() {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setEmail("owner@owner.com");
        authCredentials.setPassword("12345678");

        UserModel userExpected = new UserModel();
        userExpected.setName("Luis");
        userExpected.setLastName("Martinez");
        userExpected.setPassword("12345678");
        userExpected.setCellPhone("+574053986322");
        userExpected.setIdentificationDocument(7388348534L);
        userExpected.setEmail("owner@owner.com");
        userExpected.setRol(new RolModel("PROPIETARIO", ""));

        when(userPersistenceDomainPort.findByEmail(authCredentials.getEmail())).thenReturn(userExpected);

        String token = authUseCase.signInUseCase(authCredentials);
        assertNotNull(token);
        verify(userPersistenceDomainPort).findByEmail(authCredentials.getEmail());
        UsernamePasswordAuthenticationToken userAuth = TokenUtils.getAuthentication(token);
        Assertions.assertEquals(authCredentials.getEmail(), userAuth.getName());
    }
}