package com.reto.usuario.infrastructure.entrypoint;

import com.reto.usuario.application.dto.request.UserCustomerRequestDto;
import com.reto.usuario.application.dto.response.UserCustomerResponseDto;
import com.reto.usuario.application.dto.request.UserEmployeeRequestDto;
import com.reto.usuario.application.dto.request.UserOwnerRequestDto;
import com.reto.usuario.application.dto.response.UserEmployeeResponseDto;
import com.reto.usuario.application.dto.response.UserOwnerResponseDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.application.dto.response.UserWithFieldIdUserResponseDto;
import com.reto.usuario.application.handler.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            @ApiResponse(responseCode = "403", description = "The user does not have the admin role", content = @Content),
            @ApiResponse(responseCode = "409", description = "The email already exists", content = @Content)
    })
    @PostMapping(value = "/owner")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserOwnerResponseDto> registerUserAsOwner(@Parameter(
            description = "The user owner object to create",
            required = true,
            schema = @Schema(implementation = UserOwnerRequestDto.class))
            @RequestBody UserOwnerRequestDto userRequestDto) {
        UserOwnerResponseDto userOwnerRegistered = userService.registerUserWithOwnerRole(userRequestDto);
        return new ResponseEntity<>(userOwnerRegistered, HttpStatus.CREATED);
    }

    @Operation(summary = "Add a new user with rol employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User employee created"),
            @ApiResponse(responseCode = "400", description = "Wrong email structure"),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty"),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong"),
            @ApiResponse(responseCode = "401", description = "Username or role in the token is invalid"),
            @ApiResponse(responseCode = "403", description = "The user does not have the owner role"),
            @ApiResponse(responseCode = "404", description = "The idRol not found or role other than employee"),
            @ApiResponse(responseCode = "404", description = "The user does not have a restaurant"),
            @ApiResponse(responseCode = "409", description = "The email already exists"),
            @ApiResponse(responseCode = "502", description = "Connection refused: connect")
    })
    @PostMapping(value = "/employee")
    @PreAuthorize(value = "hasRole('PROPIETARIO')")
    public ResponseEntity<UserEmployeeResponseDto> registerUserAsEmployee(@Parameter(
            description = "Object to create an account for the restaurant employee",
            required = true, schema = @Schema(implementation = UserEmployeeRequestDto.class))
            @RequestBody UserEmployeeRequestDto userRequestToCreateEmployeeDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenWithBearerPrefix) {
        final UserEmployeeResponseDto userEmployeeRegistered = this.userService.registerUserWithEmployeeRole(userRequestToCreateEmployeeDto, tokenWithBearerPrefix);
        return new ResponseEntity<>(userEmployeeRegistered, HttpStatus.CREATED);
    }

    @Operation(summary = "Add a new user with rol customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User customer created"),
            @ApiResponse(responseCode = "400", description = "Wrong email structure"),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty"),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong"),
            @ApiResponse(responseCode = "404", description = "The rol not found or is different the value of the database"),
            @ApiResponse(responseCode = "409", description = "The email already exists")
    })
    @PostMapping(value = "/customer")
    public ResponseEntity<UserCustomerResponseDto> registerUserAsCustomer(@Parameter(
            description = "Object to create an account as customer",
            required = true, schema = @Schema(implementation = UserCustomerRequestDto.class))
            @RequestBody UserCustomerRequestDto userCustomerRequestDto) {
        final UserCustomerResponseDto accountCustomerRegistered = this.userService.registerUserWithCustomerRole(userCustomerRequestDto);
        return new ResponseEntity<>(accountCustomerRegistered, HttpStatus.CREATED);
    }

    @Operation(summary = "token verification or get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct token and found user by id"),
            @ApiResponse(responseCode = "204", description = "Correct token"),
            @ApiResponse(responseCode = "401", description = "Invalid Token")
    })
    @GetMapping(value = "/verifier")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR') or hasRole('EMPLEADO') or hasRole('PROPIETARIO') or hasRole('CLIENTE')")
    public ResponseEntity<UserResponseDto> validateTokenAndReturnAllFieldsFromUserIfValueIdUserNotIsNull(@Parameter(
            description = "The id of the user to search for", schema = @Schema(implementation = Long.class))
            @RequestParam(name = "idUser", required = false) Long idUser ) {
        if(idUser != null) {
            return new ResponseEntity<>(userService.getUserById(idUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "search user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found")
    })
    @GetMapping(value = "/get-user-by-email")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR') or hasRole('EMPLEADO') or hasRole('PROPIETARIO') or hasRole('CLIENTE')")
    public ResponseEntity<UserWithFieldIdUserResponseDto> searchUserByEmail(@Parameter(
            description = "Email unique from user", schema = @Schema(implementation = String.class))
            @RequestParam(name = "email") String email ) {
        return new ResponseEntity<>(this.userService.getUserByUniqueEmail(email), HttpStatus.OK);
    }
}
