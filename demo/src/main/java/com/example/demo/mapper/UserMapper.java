package com.example.demo.mapper;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
    List<UserResponse> toUsersResponse(List<UserEntity> userEntity);
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdationRequest userUpdationRequest);
}