package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.enums.SuccessCode;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

//    @PostMapping
//    public ApiResponse<UserResponse> sampleSaveUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
//        ApiResponse<UserResponse> userResponseApiResponse = new ApiResponse<>();
//        userResponseApiResponse.setCode(SuccessCode.SUCCESS_CODE.getCode());
//        userResponseApiResponse.setResult(userService.saveUser(userCreationRequest));
//        return userResponseApiResponse;
//    }

    @PostMapping
    public ApiResponse<UserResponse> saveUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
        return ApiResponse.<UserResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(userService.saveUser(userCreationRequest))
                .build();
    }

    @GetMapping
    public List<UserResponse> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public UserResponse updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdationRequest userUpdationRequest){
        return userService.updateUser(userId, userUpdationRequest);
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return  ApiResponse.<String>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .message(SuccessCode.SUCCESS_CODE.getMessage())
                .build();
    }
}
