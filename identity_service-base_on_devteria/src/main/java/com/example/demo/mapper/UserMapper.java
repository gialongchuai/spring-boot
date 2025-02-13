package com.example.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);
    // List<UserResponse> toUsersResponse(List<User> user);
    User toUser(UserCreationRequest userCreationRequest);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdationRequest userUpdationRequest);
}
