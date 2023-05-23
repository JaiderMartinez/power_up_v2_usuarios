package com.reto.usuario.application.handler.implementation;

import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.handler.IUserService;
import com.reto.usuario.application.mapper.request.IUserRequestMapper;
import com.reto.usuario.application.mapper.response.IUserResponseMapper;
import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.model.RolModel;
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
    public void registerUserWithOwnerRole(UserRequestDto userRequestDto) {
        userUseCasePort.registerUserWithOwnerRole(userRequestMapper.toUserModel(userRequestDto));
    }

    @Override
    public void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto) {
        userUseCasePort.registerUserWithEmployeeRole(userRequestMapper.toUserModelEmployee(userRequestToCreateEmployeeDto));
    }

    @Override
    public void registerUserWithCustomerRole(CustomerRequestDto customerRequestDto) {
        RolModel rolModel = new RolModel();
        UserModel userModel = new UserModel();
        userModel.setName(customerRequestDto.getName());
        userModel.setLastName(customerRequestDto.getLastName());
        userModel.setCellPhone(customerRequestDto.getCellPhone());
        userModel.setEmail(customerRequestDto.getEmail());
        userModel.setPassword(customerRequestDto.getPassword());
        userModel.setIdentificationDocument(customerRequestDto.getIdentificationDocument());
        rolModel.setIdRol(customerRequestDto.getIdRol());
        userModel.setRol(rolModel);
        userUseCasePort.registerUserWithCustomerRole(userModel);
    }

    @Override
    public UserResponseDto getUserById(Long idUser) {
        return userResponseMapper.toUserResponseDto(userUseCasePort.getUserById(idUser));
    }
}
