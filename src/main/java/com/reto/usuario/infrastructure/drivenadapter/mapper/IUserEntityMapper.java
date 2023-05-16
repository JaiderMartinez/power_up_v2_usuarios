package com.reto.usuario.infrastructure.drivenadapter.mapper;

import com.reto.usuario.domain.model.UserModel;
import com.reto.usuario.infrastructure.drivenadapter.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserEntity toUserEntity(UserModel userModel);

    UserModel toUserModel(UserEntity userEntity);
}
