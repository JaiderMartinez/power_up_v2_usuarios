package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.utils.TokenUtils;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import com.reto.usuario.infrastructure.drivenadapter.entity.UserEntity;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    IRolRepositoryMysql rolRepositoryMysql;

    @Autowired
    IUserRepositoryMysql userRepositoryMysql;

    @Autowired
    ObjectMapper objectMapper;

    private UserEntity userEntityOwner;

    @BeforeEach
    void setup() {
        RolEntity rolEntityWithRoleAdmin = new RolEntity();
        rolEntityWithRoleAdmin.setIdRol(1L);
        rolEntityWithRoleAdmin.setDescription("In charge of creating dish and account with role employee");
        rolEntityWithRoleAdmin.setName("PROPIETARIO");

        userEntityOwner = new UserEntity();
        userEntityOwner.setIdUser(1L);
        userEntityOwner.setName("Name Owner");
        userEntityOwner.setLastName("LastName Owner");
        userEntityOwner.setIdentificationDocument(325235325L);
        userEntityOwner.setCellPhone("5463679324");
        userEntityOwner.setEmail("owner@owner.com");
        userEntityOwner.setPassword("$2a$12$4o2roUXkPtsy5umeBGoLfuU2SWrRyLj4jZwFmYUgJCtMIPutO94ZO");
        userEntityOwner.setRol(rolEntityWithRoleAdmin);

        rolRepositoryMysql.save(rolEntityWithRoleAdmin);
        userRepositoryMysql.save(userEntityOwner);
    }

    @Test
    void test_login_withAuthCredentialsRequest_whenSystemAuthenticatesTheUserCustomer_ShouldAToken() throws Exception {

        AuthCredentialsRequest authCredentialsRequest = new AuthCredentialsRequest();
        authCredentialsRequest.setEmail("owner@owner.com");
        authCredentialsRequest.setPassword("123");

        MvcResult resultAuth = mockMvc.perform(post("/user-micro/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCredentialsRequest)))
                .andExpect(status().isOk())
                .andReturn();
        String token = resultAuth.getResponse().getContentAsString();
        token = token.substring(token.indexOf(':') + 2, token.length() - 1);
        UsernamePasswordAuthenticationToken authenticationToken = TokenUtils.getAuthentication(token);
        assertEquals(authCredentialsRequest.getEmail(), authenticationToken.getName());
        assertEquals(userEntityOwner.getRol().getName(), authenticationToken.getAuthorities().toString()
                                                        .replace("[ROLE_","").replace("]", ""));
    }

    @Test
    void test_login_withAuthCredentialsRequest_whenSystemAuthenticatesTheUser_ShouldAStatus401() throws Exception {
        AuthCredentialsRequest incorrectAuthCredentialsRequest = new AuthCredentialsRequest();

        mockMvc.perform(post("/user-micro/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incorrectAuthCredentialsRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }
}