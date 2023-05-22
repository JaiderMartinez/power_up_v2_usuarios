package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.exceptionhandler.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IRolRepositoryMysql rolRepositoryMysql;

    private static final String USERNAME_ADMIN = "admin@dmin.com";
    private static final String PASSWORD = "123";
    private static final String ROLE_ADMIN = "ADMINISTRADOR";
    private static final String USER_OWNER_API_PATH = "/user-micro/user/owner";

    @BeforeEach
    void setUp() {
        rolRepositoryMysql.save(new RolEntity(1L, "PROPIETARIO", "Restaurant owner"));
    }

    @WithMockUser(username = USERNAME_ADMIN, password = PASSWORD, roles = {ROLE_ADMIN})
    @Test
    void test_registerUserAsOwner_withCompleteUserRequestDto_shouldResponseSavedIdUserAndStatusCreated() throws Exception {
        UserRequestDto userOwner = new UserRequestDto();
        userOwner.setName("Jose");
        userOwner.setLastName("Martinez");
        userOwner.setIdentificationDocument(12323435345L);
        userOwner.setCellPhone("3154579374");
        userOwner.setEmail("owner@owner.com");
        userOwner.setPassword(PASSWORD);

        mockMvc.perform(post(USER_OWNER_API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userOwner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUser").value(1));
    }

    @WithMockUser(username = USERNAME_ADMIN, password = PASSWORD, roles = {ROLE_ADMIN})
    @Test
    void test_registerUserAsOwner_withUserRequestDtoAndInvalidEmailProperty_shouldThrowStatusBaqRequest() throws Exception {
        UserRequestDto userOwner = new UserRequestDto();
        userOwner.setName("Jose");
        userOwner.setLastName("Martinez");
        userOwner.setIdentificationDocument(12323435345L);
        userOwner.setCellPhone("3154579374");
        userOwner.setEmail("email.invalid-without-at-sign.com");
        userOwner.setPassword(PASSWORD);

        mockMvc.perform(post(USER_OWNER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOwner)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.INVALID_EMAIL_FORMAT.getMessage()));
    }
}