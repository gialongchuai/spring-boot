package com.example.demo.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.enums.SuccessCode;
import com.example.demo.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    //    @PostMapping
    //    public ApiResponse<UserResponse> sampleAddUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
    //        ApiResponse<UserResponse> userResponseApiResponse = new ApiResponse<>();
    //        userResponseApiResponse.setCode(SuccessCode.SUCCESS_CODE.getCode());
    //        userResponseApiResponse.setResult(userService.saveUser(userCreationRequest));
    //        return userResponseApiResponse;
    //    }

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @GetMapping
    List<UserResponse> getAllUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/myinfo")
    UserResponse getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdationRequest userUpdationRequest) {
        return userService.updateUser(userId, userUpdationRequest);
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .message(SuccessCode.SUCCESS_CODE.getMessage())
                .build();
    }
}
