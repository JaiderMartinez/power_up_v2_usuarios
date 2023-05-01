package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.dto.AuthCredentials;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.utils.TokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AuthUseCaseTest {

    @InjectMocks
    AuthUseCase authUseCase;

    @Mock
    IUserPersistenceDomainPort userPersistenceDomainPort;

    @Test
    void signInUseCase() {
        AuthCredentials authCredentials = new AuthCredentials();
        authCredentials.setEmail("test@gmail.com");
        authCredentials.setPassword("12345678");
        when(userPersistenceDomainPort.findByEmail(authCredentials.getEmail())).thenReturn(
                FactoryUserModelTest.userModel());
        String token = authUseCase.signInUseCase(authCredentials);
        assertNotNull(token);
        UsernamePasswordAuthenticationToken userAutheticated = TokenUtils.getAuthentication(token);
        Assertions.assertEquals(FactoryUserModelTest.userModel().getEmail(), userAutheticated.getName());
        verify(userPersistenceDomainPort).findByEmail(authCredentials.getEmail());
    }
}