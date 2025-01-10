package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> saveUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.saveUser(userCreationRequest));
        return apiResponse;
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
    public UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdationRequest userUpdationRequest){
        return userService.updateUser(userId,userUpdationRequest);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId){

        return userService.deleteUser(userId);
    }
}
