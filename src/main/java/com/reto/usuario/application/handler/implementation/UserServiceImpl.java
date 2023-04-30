package com.reto.usuario.application.handler.implementation;


import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.handler.IUserService;
import com.reto.usuario.application.mapper.request.IAuthCredentialsRequestMapper;
import com.reto.usuario.application.mapper.request.IUserRequestMapper;
import com.reto.usuario.domain.api.IUserUseCasePort;
import com.reto.usuario.domain.model.RolModel;
import com.reto.usuario.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements IUserService {

    private final IUserUseCasePort userUseCasePort;
    private final IUserRequestMapper userRequestMapper;
    private final IAuthCredentialsRequestMapper authCredentialsRequestMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerUserWithOwnerRole(UserRequestDto userRequestDto) {
        UserModel userModel = userRequestMapper.toUserModel(userRequestDto);
        userUseCasePort.registerUserWithOwnerRole(userModel);
    }

    @Override
    public String singIn(AuthCredentialsRequest authCredentialsRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authCredentialsRequest.getEmail(), authCredentialsRequest.getPassword()));

        } catch (AuthenticationException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
        return userUseCasePort.signInUseCase(
                authCredentialsRequestMapper.toAuthCredentials(authCredentialsRequest));
    }
}
