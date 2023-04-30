package com.reto.usuario.domain.spi;

import com.reto.usuario.domain.model.RolModel;

public interface IRolPersistenceDomainPort {

    RolModel findByName(String name);
}
