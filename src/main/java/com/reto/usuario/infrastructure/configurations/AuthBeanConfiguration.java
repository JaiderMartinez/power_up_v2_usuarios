package com.reto.usuario.infrastructure.configurations;

import com.reto.usuario.domain.api.IAuthUseCasePort;
import com.reto.usuario.domain.spi.persistence.IUserPersistenceDomainPort;
import com.reto.usuario.domain.spi.persistence.TokenServiceInterfacePort;
import com.reto.usuario.domain.usecase.AuthUseCase;
import com.reto.usuario.infrastructure.configurations.security.utils.TokenUtils;
import com.reto.usuario.infrastructure.drivenadapter.token.TokenServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AuthBeanConfiguration {

    private final IUserPersistenceDomainPort userPersistenceDomainPort;
    private final TokenUtils tokenUtils;

    @Bean
    public TokenServiceInterfacePort tokenServiceInterfacePort() {
        return new TokenServiceAdapter(this.tokenUtils);
    }

    @Bean
    public IAuthUseCasePort authUseCasePort() {
        return new AuthUseCase(this.userPersistenceDomainPort, tokenServiceInterfacePort());
    }
}
