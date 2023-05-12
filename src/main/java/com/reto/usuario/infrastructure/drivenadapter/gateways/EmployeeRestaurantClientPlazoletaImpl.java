package com.reto.usuario.infrastructure.drivenadapter.gateways;

import com.reto.usuario.domain.dto.EmployeeRestaurantClientRequestDto;
import com.reto.usuario.domain.exceptions.TokenInvalidException;
import com.reto.usuario.domain.gateways.IEmployeeRestaurantClientPlazoleta;
import com.reto.usuario.infrastructure.drivenadapter.exceptions.EmployeeRestaurantExistException;
import com.reto.usuario.infrastructure.drivenadapter.exceptions.RestaurantNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.SocketException;

@Service
public class EmployeeRestaurantClientPlazoletaImpl implements IEmployeeRestaurantClientPlazoleta {

    private static final String BASE_URL = "http://localhost:9090/services-owner-restaurant";
    WebClient webClient = WebClient.builder().baseUrl(BASE_URL).build();

    @Override
    public void saveUserEmployeeToARestaurant(EmployeeRestaurantClientRequestDto employeeRestaurantClientRequestDto, String tokenWithPrefixBearer) {
        webClient.post().uri(uriBuilder -> uriBuilder.path("/employee")
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
                        return Mono.error(new RestaurantNotFoundException("No restaurant found for you as owner"));
                    } else if(clientResponse.statusCode().equals(HttpStatus.CONFLICT)) {
                        return Mono.error(new EmployeeRestaurantExistException("The employee user already exists in the restaurant."));
                    } else {
                        return Mono.error(new SocketException("Unexpected error"));
                    }
                })
                .block();
    }
}
