package com.reto.usuario.application.handler.implementation;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.response.TokenResponseDto;
import com.reto.usuario.application.handler.IAuthService;
import com.reto.usuario.application.mapper.request.IAuthCredentialsRequestMapper;
import com.reto.usuario.domain.api.IAuthUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements IAuthService {


    private final IAuthCredentialsRequestMapper authCredentialsRequestMapper;
    private final AuthenticationManager authenticationManager;
    private final IAuthUseCasePort authUseCasePort;

    @Override
    public TokenResponseDto singIn(AuthCredentialsRequest authCredentialsRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authCredentialsRequest.getEmail(), authCredentialsRequest.getPassword()));

        } catch (AuthenticationException e) {
            throw new AuthenticationCredentialsNotFoundException(e.getMessage());
        }
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(authUseCasePort.signInUseCase(
                authCredentialsRequestMapper.toAuthCredentials(authCredentialsRequest)));
        return tokenResponseDto;
    }
}
