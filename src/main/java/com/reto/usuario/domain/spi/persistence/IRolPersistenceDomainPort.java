package com.reto.usuario.domain.spi.persistence;

import com.reto.usuario.domain.model.RolModel;

public interface IRolPersistenceDomainPort {

    RolModel findByName(String name);

    RolModel findByIdRol(Long idRol);
}
