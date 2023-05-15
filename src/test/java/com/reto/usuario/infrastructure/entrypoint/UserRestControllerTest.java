package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.response.UserResponseDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {

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

        userRepositoryMysql.save(FactoryEntityAndRequest.userAdmin());
        userRepositoryMysql.save(FactoryEntityAndRequest.userOwner());
    }

    @WithMockUser(username = "admin@admin.com", password = "123", roles = {"ADMINISTRADOR"})
    @Test
    void test_registerUserAsOwner_withObjectUserRequestToCreateEmployeeDto_whenSystemCreateAccountWithOwnerRole_ShouldStatusCreated() throws Exception {
        mockMvc.perform(post("/user-micro/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FactoryEntityAndRequest.objectUserOwnerRequest())))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "owner@owner.com", password = "123", roles = {"PROPIETARIO"})
    @Test
    void test_userVerifierUserByToken_withLongIdUser_whenSystemFindUserById_ShouldTheUserWithIdOne() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(FactoryEntityAndRequest.userAdmin().getName());
        userResponseDto.setLastName(FactoryEntityAndRequest.userAdmin().getLastName());
        userResponseDto.setCellPhone(FactoryEntityAndRequest.userAdmin().getCellPhone());
        userResponseDto.setEmail(FactoryEntityAndRequest.userAdmin().getEmail());
        userResponseDto.setIdentificationDocument(FactoryEntityAndRequest.userAdmin().getIdentificationDocument());
        userResponseDto.setRol(FactoryEntityAndRequest.userAdmin().getRol().getName());

        MvcResult resultUserFound = mockMvc.perform(get("/user-micro/user/verifier?idUser=" + FactoryEntityAndRequest.userAdmin().getIdUser())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertTrue(resultUserFound.getResponse().getContentAsString().contentEquals(objectMapper.writeValueAsString(userResponseDto)));
    }
}