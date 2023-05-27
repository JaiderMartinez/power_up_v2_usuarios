package com.reto.usuario.infrastructure.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reto.usuario.application.dto.request.UserRequestDto;
import com.reto.usuario.application.dto.request.UserRequestToCreateEmployeeDto;
import com.reto.usuario.domain.gateways.IEmployeeRestaurantClientSmallSquare;
import com.reto.usuario.infrastructure.drivenadapter.entity.RolEntity;
import com.reto.usuario.infrastructure.drivenadapter.entity.UserEntity;
import com.reto.usuario.infrastructure.drivenadapter.repository.IRolRepositoryMysql;
import com.reto.usuario.infrastructure.drivenadapter.repository.IUserRepositoryMysql;
import com.reto.usuario.infrastructure.exceptionhandler.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IRolRepositoryMysql rolRepositoryMysql;

    @Autowired
    private IUserRepositoryMysql userRepositoryMysql;

    @MockBean
    private IEmployeeRestaurantClientSmallSquare employeeRestaurantClientSmallSquare;

    private static final String USERNAME_ADMIN = "admin@dmin.com";
    private static final String USERNAME_OWNER = "owner@owner.com";
    private static final String PASSWORD = "123";
    private static final String ROLE_ADMIN = "ADMINISTRADOR";
    private static final String ROLE_OWNER = "PROPIETARIO";
    private static final String USER_OWNER_API_PATH = "/user-micro/user/owner";
    private static final String USER_EMPLOYEE_API_PATH = "/user-micro/user/employee";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvd25lckBvd25lci5jb20iLCJpYXQiOjE2ODUxNTQ5NDAsImV4cCI6MTY4Nzc0Njk0MCwibGFzdE5hbWUiOiJmb3Jlcm8iLCJuYW1lIjoiam9oYW5hIiwicm9sIjpbIlJPTEVfUFJPUElFVEFSSU8iXX0.-V_b7X1f72gW7swmRFdYz0KUGQOqIO_YObdeKaHh0OA";

    @BeforeEach
    void setUp() {
        rolRepositoryMysql.save(new RolEntity(1L, "PROPIETARIO", "Restaurant owner"));
        rolRepositoryMysql.save(new RolEntity(2L, "EMPLEADO", "Restaurant employee"));
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
    void test_registerUserAsOwner_withFieldEmailIsInvalidInUserRequestDto_shouldThrowStatusBadRequest() throws Exception {
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

    @WithMockUser(username = USERNAME_ADMIN, password = PASSWORD, roles = {ROLE_ADMIN})
    @Test
    void test_registerUserAsOwner_withFieldsEmptyInUserRequestDto_shouldThrowStatusBadRequest() throws Exception {
        UserRequestDto userOwner = new UserRequestDto();
        userOwner.setName("");
        userOwner.setLastName("");
        userOwner.setIdentificationDocument(null);
        userOwner.setCellPhone("");
        userOwner.setEmail("email@example.com");
        userOwner.setPassword("");

        mockMvc.perform(post(USER_OWNER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOwner)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.EMPTY_FIELDS.getMessage()));
    }

    @WithMockUser(username = USERNAME_ADMIN, password = PASSWORD, roles = {ROLE_ADMIN})
    @Test
    void test_registerUserAsOwner_withInvalidCellPhoneInUserRequestDto_shouldThrowStatusBadRequest() throws Exception {
        UserRequestDto userOwner = new UserRequestDto();
        userOwner.setName("Jose");
        userOwner.setLastName("Martinez");
        userOwner.setIdentificationDocument(12323435345L);
        userOwner.setIdentificationDocument(null);
        userOwner.setCellPhone("+5792385492378345");
        userOwner.setEmail("owner@owner.com");
        userOwner.setPassword(PASSWORD);

        mockMvc.perform(post(USER_OWNER_API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userOwner)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.EMPTY_FIELDS.getMessage()));
    }

    @WithMockUser(username = USERNAME_ADMIN, password = PASSWORD, roles = {ROLE_ADMIN})
    @Test
    void test_registerUserAsOwner_withExistingEmailInUserRequestDto_shouldThrowStatusConflict() throws Exception {
        userRepositoryMysql.save(new UserEntity(1L, "Jose", "Martinez", 12323435345L,
                "3154579374", "owner@owner.com", PASSWORD, new RolEntity(1L, "PROPIETARIO", "Restaurant owner")));

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
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.EMAIL_EXISTS.getMessage()));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withAllFieldsCompleteUserRequestToCreateEmployeeDto_shouldResponseStatusCreatedAndFieldIdUserSavedInTheDataBase() throws Exception {
        this.userRepositoryMysql.save(new UserEntity(1L, "Jose", "Santiago", 1243545623L,
                "+573154579374", USERNAME_OWNER, PASSWORD, new RolEntity(1L, ROLE_OWNER, "Restaurant owner")));

        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("Jose", "Martinez", 193235345L, "3154579374",
                                                                "employee@employee.com", PASSWORD, 2L);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUser").value(2L));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withFieldEmailInvalidStructure_shouldReturnStatusBadRequest() throws Exception {
        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("Jose", "Martinez", 193235345L, "3154579374",
                "email-without-at-sign.employee.com", PASSWORD, 2L);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.INVALID_EMAIL_FORMAT.getMessage()));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withAllFieldsEmptyExceptEmailInTheObjectAsUserRequestToCreateEmployeeDto_shouldReturnStatusBadRequest() throws Exception {
        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("", "", null, "",
                "employee@employee.com", null, null);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.EMPTY_FIELDS.getMessage()));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withFieldCellPhoneFormatIsInvalid_shouldReturnStatusBadRequest() throws Exception {
        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("Jose", "Martinez", 193235345L,
                "31597324error", "employee@employee.com", PASSWORD, 2L);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.INVALID_CELL_PHONE_FORMAT.getMessage()));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withValueFromFieldIdRolNotExistsInTheDataBase_shouldReturnStatusNotFound() throws Exception {
        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("Jose", "Martinez", 193235345L,
                "3159732432", "employee@employee.com", PASSWORD, 500L);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.ROL_NOT_FOUND.getMessage()));
    }

    @WithMockUser(username = USERNAME_OWNER, password = PASSWORD, roles = {ROLE_OWNER})
    @Test
    void test_registerUserAsEmployee_withFieldEmailExistsInTheDataBase_shouldReturnStatusConflict() throws Exception {
        this.userRepositoryMysql.save(new UserEntity(1L, "Jose", "Santiago", 1243545623L,
                "+573154579374", USERNAME_OWNER, PASSWORD, new RolEntity(1L, ROLE_OWNER, "Restaurant owner")));

        UserRequestToCreateEmployeeDto userRequestToCreateEmployee = new UserRequestToCreateEmployeeDto("Jose", "Martinez", 193235345L,
                "3159732432", USERNAME_OWNER, PASSWORD, 2L);

        mockMvc.perform(post(USER_EMPLOYEE_API_PATH)
                        .header(HttpHeaders.AUTHORIZATION, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestToCreateEmployee)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(ExceptionResponse.EMAIL_EXISTS.getMessage()));
    }
}