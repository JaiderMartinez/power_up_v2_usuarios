package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.utils.TokenUtils;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
import com.reto.usuario.infrastructure.entrypoint.factory.FactoryEntityAndRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @BeforeEach
    void setup() {
        rolRepositoryMysql.save(FactoryEntityAndRequest.roleEntityAdmin());
        rolRepositoryMysql.save(FactoryEntityAndRequest.roleEntityOwner());
        rolRepositoryMysql.save(FactoryEntityAndRequest.roleEntityEmployee());
        rolRepositoryMysql.save(FactoryEntityAndRequest.roleEntityCustomer());
        userRepositoryMysql.save(FactoryEntityAndRequest.userOwner());
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
        Assertions.assertEquals(authCredentialsRequest.getEmail(), authenticationToken.getName());
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