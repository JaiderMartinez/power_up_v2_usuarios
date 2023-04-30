package com.reto.usuario.domain.spi;

import com.reto.usuario.domain.model.UserModel;

import java.util.Optional;

public interface IUserPersistenceDomainPort {

    UserModel saveUser(UserModel usuarioDomain);

    UserModel findByEmail(String email);

    boolean existsByEmail(String email);
}
