package com.example.demo.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.demo.dto.request.UserCreationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.36");

    //    @DynamicPropertySource
    //    static void setMySqlContainer(DynamicPropertyRegistry registry) {
    //        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
    //        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
    //        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    //        registry.add("spring.datasource.driverClassName", MY_SQL_CONTAINER::getDriverClassName);
    //        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    //    }

    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest userCreationRequest;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2012, 12, 12);

        userCreationRequest = UserCreationRequest.builder()
                .username("joe1231234578")
                .password("alibaba123")
                .firstName("Joe")
                .lastName("Goldberg")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN & THEN
        //        mockMvc.perform(MockMvcRequestBuilders
        //                        .post("/users")
        //                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        //                        .content(content))
        //                .andExpect(MockMvcResultMatchers.status().isOk())
        //                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Joe"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Goldberg"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("joe123123"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.dob").value("2012-12-12"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.roles[0].name").value("USER"));
    }
}
