package com.reto.usuario.infrastructure.entrypoint;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.response.TokenResponseDto;
import com.reto.usuario.application.handler.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user-micro/auth")
public class AuthRestController {

    private final IAuthService authService;

    @Operation(summary = "Login to get token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session started", content = @Content),
            @ApiResponse(responseCode = "401", description = "Bad credentials", content = @Content),
            @ApiResponse(responseCode = "401", description = "Email not found", content = @Content)
    })
    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponseDto> login(@Parameter(
            description = "The credentials of the user to login",
            required = true,
            schema = @Schema(implementation = AuthCredentialsRequest.class))
            @RequestBody AuthCredentialsRequest authCredentialsRequest) {
        return ResponseEntity.ok(authService.singIn(authCredentialsRequest));
    }

}
