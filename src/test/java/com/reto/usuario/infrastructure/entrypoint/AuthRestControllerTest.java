package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.AuthCredentialsRequest;
import com.reto.usuario.domain.usecase.FactoryUserModelTest;
import com.reto.usuario.domain.utils.TokenUtils;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user-micro/user/customer")
                        .content(objectMapper.writeValueAsString(FactoryUserModelTest.userModel()))
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void login() throws Exception {
        AuthCredentialsRequest authCredentialsRequest = new AuthCredentialsRequest();
        authCredentialsRequest.setEmail("test@example.com");
        authCredentialsRequest.setPassword("12345678");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user-micro/auth/login")
                    .content(objectMapper.writeValueAsString(authCredentialsRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultLogin = result.getResponse().getContentAsString();
        String token = resultLogin.substring(resultLogin.indexOf(":") + 2, resultLogin.lastIndexOf("\""));
        UsernamePasswordAuthenticationToken auth = TokenUtils.getAuthentication(token);
        Assertions.assertEquals(authCredentialsRequest.getEmail(), auth.getName());
    }
}