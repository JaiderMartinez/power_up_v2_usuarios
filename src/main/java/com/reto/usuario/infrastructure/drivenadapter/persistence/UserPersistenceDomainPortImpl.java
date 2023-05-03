package com.reto.usuario.infrastructure.drivenadapter.persistence;

import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.domain.spi.IUserPersistenceDomainPort;
import com.reto.usuario.infrastructure.drivenadapter.mapper.IUserEntityMapper;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.exceptions.EmailNotFoundException;


public class UserPersistenceDomainPortImpl implements IUserPersistenceDomainPort {

    private final IUserRepositoryMysql userRepositoryMysql;
    private final IUserEntityMapper userEntityMapper;

    public UserPersistenceDomainPortImpl(IUserRepositoryMysql userRepositoryMysql, IUserEntityMapper userEntityMapper) {
        this.userRepositoryMysql = userRepositoryMysql;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        return userEntityMapper.toUserModel(
                userRepositoryMysql.save(userEntityMapper.toUserEntity(userModel)));
    }

    @Override
    public UserModel findByEmail(String email) {
        UserModel userModel = userEntityMapper.toUserModel(
                userRepositoryMysql.findByEmail(email).orElse(null));
        if (userModel == null) {
            throw new EmailNotFoundException("Email not found");
        }
        return userModel;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryMysql.existsByEmail(email);
    }

    @Override
    public UserModel findById(Long idUser) {
        return userEntityMapper.toUserModel(
                userRepositoryMysql.findById(idUser).orElse(null));
    }
}
