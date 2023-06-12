package com.reto.usuario.application.handler.implementation;

import com.reto.usuario.application.dto.request.UserCustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserCustomerResponseDto;
import com.reto.usuario.application.dto.response.UserEmployeeResponseDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.dto.response.UserWithFieldIdUserResponseDto;
import com.reto.usuario.application.handler.IUserService;
import com.reto.usuario.application.mapper.request.IUserRequestMapper;
import com.reto.usuario.application.mapper.response.IUserResponseMapper;
import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements IUserService {

    private final IUserUseCasePort userUseCasePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;

    @Override
    public UserOwnerResponseDto registerUserWithOwnerRole(UserRequestDto userRequestDto) {
        final UserModel userRequestModel = this.userRequestMapper.toUserModel(userRequestDto);
        final UserModel userRegisteredModel = this.userUseCasePort.registerUserWithOwnerRole(userRequestModel );
        return this.userResponseMapper.toUserOwnerResponseDto(userRegisteredModel );
    }

    @Override
    public UserEmployeeResponseDto registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto, String tokenWithPrefixBearer) {
        final UserModel userEmployeeRequestModel = this.userRequestMapper.toUserModelEmployee(userRequestToCreateEmployeeDto);
        final UserModel userEmployeeRegisteredModel = this.userUseCasePort.registerUserWithEmployeeRole(userEmployeeRequestModel, tokenWithPrefixBearer, userRequestToCreateEmployeeDto.getIdRestaurant());
        return userResponseMapper.toUserEmployeeResponseDto(userEmployeeRegisteredModel);
    }

    @Override
    public UserCustomerResponseDto registerUserWithCustomerRole(UserCustomerRequestDto userCustomerRequestDto) {
        final UserModel userCustomerRequestModel = this.userRequestMapper.userCustomerRequestDtoToUserModel(userCustomerRequestDto);
        final UserModel userCustomerRegisteredModel = this.userUseCasePort.registerUserWithCustomerRole(userCustomerRequestModel);
        return this.userResponseMapper.userModeltoUserCustomerResponseDto(userCustomerRegisteredModel);
    }

    @Override
    public UserResponseDto getUserById(Long idUser) {
        return userResponseMapper.toUserResponseDto(userUseCasePort.getUserById(idUser));
    }

    @Override
    public UserWithFieldIdUserResponseDto getUserByUniqueEmail(String email) {
        return this.userResponseMapper.toUserWithFieldIdUserResponseDto(this.userUseCasePort.findUserByEmail(email));
    }
}
