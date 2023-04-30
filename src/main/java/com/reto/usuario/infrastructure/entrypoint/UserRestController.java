package com.reto.usuario.infrastructure.entrypoint;

import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.handler.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/user-micro/user")
public class UserRestController {

    private final IUserService userService;

    @Operation(summary = "Add a new User with rol owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong email structure", content = @Content),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty", content = @Content),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong", content = @Content),
            @ApiResponse(responseCode = "409", description = "The email already exists", content = @Content)
    })
    @PreAuthorize(value = "hasRole('ADMINISTRADOR')")
    @PostMapping(value = "/")
    public ResponseEntity<Void> registerUserAsOwner(@Parameter(
            description = "The user owner object to create",
            required = true,
            schema = @Schema(implementation = UserRequestDto.class))
            @RequestBody UserRequestDto userRequestDto) {
        userService.registerUserWithOwnerRole(userRequestDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Login to get token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session started", content = @Content),
            @ApiResponse(responseCode = "401", description = "Bad credentials", content = @Content),
            @ApiResponse(responseCode = "401", description = "Email not found", content = @Content)
    })
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@Parameter(
            description = "The credentials of the user to login",
            required = true,
            schema = @Schema(implementation = AuthCredentialsRequest.class))
            @RequestBody AuthCredentialsRequest authCredentialsRequest) {
        return ResponseEntity.ok(userService.singIn(authCredentialsRequest));
    }

    @Operation(summary = "token verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct token", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content)
    })
    @GetMapping(value = "/verifier")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR') or hasRole('EMPLEADO') or hasRole('PROPIETARIO') or hasRole('CLIENTE')")
    public ResponseEntity<Void> userVerifierByToken( ) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
