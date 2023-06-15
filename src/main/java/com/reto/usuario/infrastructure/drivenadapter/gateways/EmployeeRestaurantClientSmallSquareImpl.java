package com.reto.usuario.infrastructure.drivenadapter.gateways;

import com.reto.usuario.domain.dto.EmployeeRestaurantClientRequestDto;
import com.reto.usuario.domain.exceptions.TokenInvalidException;
import com.reto.usuario.domain.gateways.IEmployeeRestaurantClientSmallSquare;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.SocketException;

@RequiredArgsConstructor
@Service
public class EmployeeRestaurantClientSmallSquareImpl implements IEmployeeRestaurantClientSmallSquare {

    private final WebClient webClient;

    @Override
    public void saveUserEmployeeToARestaurant(EmployeeRestaurantClientRequestDto employeeRestaurantClientRequestDto, String tokenWithPrefixBearer) {
        webClient.post().uri(uriBuilder -> uriBuilder.path("restaurant/employee")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, tokenWithPrefixBearer)
                .bodyValue(employeeRestaurantClientRequestDto)
                .exchangeToMono( clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.CREATED)) {
                        return Mono.empty();
                    } else if(clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        return Mono.error(new TokenInvalidException("Username or role in the token is invalid"));
                    } else if(clientResponse.statusCode().equals(HttpStatus.FORBIDDEN)) {
                        return Mono.error(new AccessDeniedException("Access denied by insufficient permissions"));
                    } else if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(new NullPointerException("You don't own any restaurant"));
                    } else {
                        return Mono.error(new SocketException("Unexpected error"));
                    }
                })
                .block();
    }
}
