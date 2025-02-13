package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
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
    void initData() {
        dob = LocalDate.of(1990, 12, 1);

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
    void createUser_validRequest_success() {
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
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    @WithMockUser(username = "joebye")
    void getMyInfo_valid_success() {
        // GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // WHEN
        var user = userService.getMyInfo();

        Assertions.assertThat(user.getId()).isEqualTo("3fc3dfed-2de8-4588");
        Assertions.assertThat(user.getUsername()).isEqualTo("joebye");
    }

    @Test
    @WithMockUser(username = "joebye")
    void getMyInfo_notFoundUser_fail() {
        // GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }
}
