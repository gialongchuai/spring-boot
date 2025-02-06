package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;
    private User user;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(1990, 12,1);

        userCreationRequest = UserCreationRequest.builder()
                .username("joebye")
                .password("joe123123")
                .firstName("Joe")
                .lastName("Goldberg")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("3fc3dfed-2de8-4588")
                .username("joebye")
                .firstName("Joe")
                .lastName("Goldberg")
                .dob(dob)
                .build();

        user = User.builder()
                .id("3fc3dfed-2de8-4588")
                .username("joebye")
                .firstName("Joe")
                .lastName("Goldberg")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var userResponse = userService.createUser(userCreationRequest);

        // THEN
        Assertions.assertThat(userResponse.getId()).isEqualTo("3fc3dfed-2de8-4588");
        Assertions.assertThat(userResponse.getUsername()).isEqualTo("joebye");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}
