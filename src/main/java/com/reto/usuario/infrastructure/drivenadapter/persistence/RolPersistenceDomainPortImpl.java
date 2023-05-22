package com.reto.usuario.infrastructure.drivenadapter.persistence;

import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.spi.IRolPersistenceDomainPort;
import com.reto.usuario.infrastructure.drivenadapter.mapper.IRolEntityMapper;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RolPersistenceDomainPortImpl implements IRolPersistenceDomainPort {

    private final IRolRepositoryMysql rolRepositoryMysql;
    private final IRolEntityMapper rolEntityMapper;

    @Override
    public RolModel findByName(String name) {
        return rolEntityMapper.toRolModel(rolRepositoryMysql.findByName(name));
    }

    @Override
    public RolModel findByIdRol(Long idRol) {
        return rolEntityMapper.toRolModel(rolRepositoryMysql.findById(idRol).orElse(null));
    }
}
