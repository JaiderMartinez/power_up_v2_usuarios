package com.reto.usuario.infrastructure.entrypoint;

import com.reto.usuario.application.dto.request.CustomerRequestDto;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
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

    @Operation(summary = "Add a new user with rol owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User owner created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong email structure", content = @Content),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty", content = @Content),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong", content = @Content),
            @ApiResponse(responseCode = "403", description = "The user does not have the admin role", content = @Content),
            @ApiResponse(responseCode = "409", description = "The email already exists", content = @Content)
    })
    @PostMapping(value = "/")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> registerUserAsOwner(@Parameter(
            description = "The user owner object to create", required = true,
            schema = @Schema(implementation = UserRequestDto.class)) @RequestBody UserRequestDto userRequestDto) {
        userService.registerUserWithOwnerRole(userRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Add a new User with rol employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User customer created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong email structure", content = @Content),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty", content = @Content),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong", content = @Content),
            @ApiResponse(responseCode = "403", description = "The user does not have the owner role", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rol Not Found or role other than employee", content = @Content),
            @ApiResponse(responseCode = "409", description = "The email already exists", content = @Content)
    })
    @PostMapping(value = "/employee")
    @PreAuthorize(value = "hasRole('PROPIETARIO')")
    public ResponseEntity<Void> registerUserAsEmployee(@RequestBody UserRequestToCreateEmployeeDto userRequestToCreateEmployeeDto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String tokenWithBearerPrefix) {
        userService.registerUserWithEmployeeRole(userRequestToCreateEmployeeDto, tokenWithBearerPrefix);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Add a new User with rol customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User employee created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong email structure", content = @Content),
            @ApiResponse(responseCode = "400", description = "Fields cannot be empty", content = @Content),
            @ApiResponse(responseCode = "400", description = "The cell phone format is wrong", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rol Not Found or role other than customer", content = @Content),
            @ApiResponse(responseCode = "409", description = "The email already exists", content = @Content)
    })
    @PostMapping(value = "/customer")
    public ResponseEntity<Void> registerUserAsCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        userService.registerUserWithCustomerRole(customerRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "token verification or get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correct token and found user by id", content = @Content),
            @ApiResponse(responseCode = "204", description = "Correct token", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid Token", content = @Content)
    })
    @GetMapping(value = "/verifier")
    @PreAuthorize(value = "hasRole('ADMINISTRADOR') or hasRole('EMPLEADO') or hasRole('PROPIETARIO') or hasRole('CLIENTE')")
    public ResponseEntity<UserResponseDto> userVerifierUserByToken(@Parameter(
            description = "The id of the user to search for", schema = @Schema(implementation = Long.class))
            @RequestParam(name = "idUser", required = false) Long idUser ) {
        if(idUser != null) {
            return new ResponseEntity<>(userService.getUserById(idUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
