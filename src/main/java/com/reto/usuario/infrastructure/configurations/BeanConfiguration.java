package com.reto.usuario.infrastructure.configurations;

import com.reto.usuario.domain.api.IAuthUseCasePort;
import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.domain.usecase.AuthUseCase;
import com.reto.usuario.domain.usecase.UserUseCase;
import com.reto.usuario.infrastructure.drivenadapter.mapper.IRolEntityMapper;
import com.reto.usuario.infrastructure.drivenadapter.mapper.IUserEntityMapper;
import com.reto.usuario.infrastructure.drivenadapter.persistence.RolPersistenceDomainPortImpl;
import com.reto.usuario.infrastructure.drivenadapter.persistence.UserPersistenceDomainPortImpl;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BeanConfiguration {

    private final IUserRepositoryMysql userRepositoryMysql;
    private final IUserEntityMapper userEntityMapper;
    private final IRolRepositoryMysql rolRepositoryMysql;
    private final IRolEntityMapper rolEntityMapper;

    @Bean
    public IUserPersistenceDomainPort userPersistencePort() {
        return new UserPersistenceDomainPortImpl(userRepositoryMysql, userEntityMapper);
    }

    @Bean
    public IRolPersistenceDomainPort rolesPersistencePort() {
        return new RolPersistenceDomainPortImpl(rolRepositoryMysql, rolEntityMapper);
    }

    @Bean
    public IUserUseCasePort userUseCasePort() {
        return new UserUseCase(userPersistencePort(), rolesPersistencePort());
    }

    @Bean
    public IAuthUseCasePort authUseCasePort() {
        return new AuthUseCase(userPersistencePort());
    }

}
