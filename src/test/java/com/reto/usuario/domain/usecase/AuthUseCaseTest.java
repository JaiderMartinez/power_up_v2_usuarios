package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.dto.AuthCredentials;
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
        authCredentials.setEmail("test@gmail.com");
        authCredentials.setPassword("12345678");

        when(userPersistenceDomainPort.findByEmail(authCredentials.getEmail())).thenReturn(
                FactoryUserModelTest.userModel());

        String token = authUseCase.signInUseCase(authCredentials);
        assertNotNull(token);
        verify(userPersistenceDomainPort).findByEmail(authCredentials.getEmail());
        UsernamePasswordAuthenticationToken userAuth = TokenUtils.getAuthentication(token);
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userAuth.getName());
    }
}