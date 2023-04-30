package com.reto.usuario.infrastructure.drivenadapter.mapper;

import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IRolEntityMapper {

    RolModel toRolModel(RolEntity rolEntity);
}
