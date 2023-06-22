package com.reto.usuario.domain.usecase;

import com.reto.usuario.domain.model.AuthCredentialModel;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.persistence.IUserPersistenceDomainPort;
import com.reto.usuario.domain.spi.persistence.TokenServiceInterfacePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @InjectMocks
    private AuthUseCase authUseCase;

    @Mock
    private IUserPersistenceDomainPort userPersistenceDomainPort;

    @Mock
    private TokenServiceInterfacePort tokenServiceInterfacePort;

    private static final String TOKEN = "token without prefix";

    @Test
    void test_signInUseCase_withObjectAsAuthCredentialsValid_shouldReturnATokenAccess() {
        //Given
        RolModel rolModelExpected = new RolModel();
        rolModelExpected.setName("PROPIETARIO");
        UserModel userModelExpected = new UserModel();
        userModelExpected.setName("Luis");
        userModelExpected.setLastName("Martinez");
        userModelExpected.setPassword("12345678");
        userModelExpected.setCellPhone("+574053986322");
        userModelExpected.setIdentificationDocument(7388348534L);
        userModelExpected.setEmail("test@gmail.com");
        userModelExpected.setRol(rolModelExpected);

        AuthCredentialModel authCredentials = new AuthCredentialModel();
        authCredentials.setEmail("test@gmail.com");
        authCredentials.setPassword("12345678");
        List<String> authority = new ArrayList<>();
        authority.add("ROLE_PROPIETARIO");
        when(this.userPersistenceDomainPort.findByEmail(authCredentials.getEmail())).thenReturn(userModelExpected);
        when(this.tokenServiceInterfacePort.generateTokenAccess(authCredentials.getEmail(), authority, "Luis",
                "Martinez")).thenReturn(TOKEN);
        //When
        String resulTokenGenerated = this.authUseCase.signInUseCase(authCredentials);
        //Then
        assertNotNull(resulTokenGenerated);
        verify(this.userPersistenceDomainPort).findByEmail(authCredentials.getEmail());
        verify(this.tokenServiceInterfacePort).generateTokenAccess(authCredentials.getEmail(),authority,"Luis","Martinez");
        assertEquals(userModelExpected.getEmail(), authCredentials.getEmail());
        assertEquals(TOKEN, resulTokenGenerated);
    }
}