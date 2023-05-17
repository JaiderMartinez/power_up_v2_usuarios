package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.response.UserResponseDto;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import com.reto.usuario.infrastructure.drivenadapter.entity.UserEntity;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
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

    private UserEntity userEntityAdmin;

    @BeforeEach
    void setup() {
        RolEntity rolAdmin = new RolEntity(1L, "ADMINISTRADOR", "");
        rolRepositoryMysql.save(rolAdmin);
        rolRepositoryMysql.save(new RolEntity(2L, "PROPIETARIO", ""));

        userEntityAdmin = new UserEntity();
        userEntityAdmin.setName("Name Admin");
        userEntityAdmin.setLastName("LastName Admin");
        userEntityAdmin.setCellPhone("5463679324");
        userEntityAdmin.setEmail("admin@admin.com");
        userEntityAdmin.setIdentificationDocument(325235325L);
        userEntityAdmin.setPassword("$2a$12$4o2roUXkPtsy5umeBGoLfuU2SWrRyLj4jZwFmYUgJCtMIPutO94ZO");
        userEntityAdmin.setRol(rolAdmin);

        userRepositoryMysql.save(userEntityAdmin);
    }

    @WithMockUser(username = "admin@admin.com", password = "123", roles = {"ADMINISTRADOR"})
    @Test
    void test_registerUserAsOwner_withObjectUserRequest_ShouldResponseRegisterUserAsOwnerSuccess() throws Exception {
        UserRequestDto userOwner = new UserRequestDto();
        userOwner.setName("Owner");
        userOwner.setLastName("LastName");
        userOwner.setCellPhone("3125678902");
        userOwner.setIdentificationDocument(353534292L);
        userOwner.setEmail("new-owner@owner.com");
        userOwner.setPassword("123");

        mockMvc.perform(post("/user-micro/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOwner)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "owner@owner.com", password = "123", roles = {"PROPIETARIO"})
    @Test
    void test_userVerifierUserByToken_withValidTokenForUserOne_ShouldReturnUserFoundWithIdOne() throws Exception {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName("Name Admin");
        userResponseDto.setLastName("LastName Admin");
        userResponseDto.setCellPhone("5463679324");
        userResponseDto.setEmail("admin@admin.com");
        userResponseDto.setIdentificationDocument(325235325L);
        userResponseDto.setRol("ADMINISTRADOR");

        MvcResult resultUserFound = mockMvc.perform(get("/user-micro/user/verifier?idUser=" + userEntityAdmin.getIdUser())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Assertions.assertTrue(resultUserFound.getResponse().getContentAsString().contentEquals(objectMapper.writeValueAsString(userResponseDto)));
    }
}