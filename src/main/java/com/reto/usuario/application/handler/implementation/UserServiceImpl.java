package com.reto.usuario.application.handler.implementation;


import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.handler.IUserService;
import com.reto.usuario.application.mapper.request.IUserRequestMapper;
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

    @Override
    public void registerUserWithOwnerRole(UserRequestDto userRequestDto) {
        UserModel userModel = userRequestMapper.toUserModel(userRequestDto);
        userUseCasePort.registerUserWithOwnerRole(userModel);
    }

    @Override
    public void registerUserWithEmployeeRole(UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto) {
        RolModel rolModel = new RolModel();
        UserModel userModel = new UserModel();
        userModel.setName(userRequestToCreateEmployeeDto.getName());
        userModel.setLastName(userRequestToCreateEmployeeDto.getLastName());
        userModel.setCellPhone(userRequestToCreateEmployeeDto.getCellPhone());
        userModel.setEmail(userRequestToCreateEmployeeDto.getEmail());
        userModel.setPassword(userRequestToCreateEmployeeDto.getPassword());
        userModel.setIdentificationDocument(userRequestToCreateEmployeeDto.getIdentificationDocument());
        rolModel.setIdRol(userRequestToCreateEmployeeDto.getIdRol());
        userModel.setRol(rolModel);
        userUseCasePort.registerUserWithEmployeeRole(userModel);
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
        UserModel userModel = userUseCasePort.getUserById(idUser);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(userModel.getName());
        userResponseDto.setLastName(userModel.getLastName());
        userResponseDto.setCellPhone(userModel.getCellPhone());
        userResponseDto.setEmail(userModel.getEmail());
        userResponseDto.setRol(userModel.getRol().getName());
        return userResponseDto;
    }
}
