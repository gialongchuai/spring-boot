package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse saveUser(UserCreationRequest userCreationRequest){
        if(userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserResponse userResponse = userMapper.toUserResponse(userCreationRequest);

        return userRepository.save(userResponse);
    }

    public List<UserEntity> getAllUser(){
        return userRepository.findAll();
    }

    public UserEntity getUser(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public UserEntity updateUser(String userId, UserUpdationRequest userUpdationRequest) {
        UserEntity userEntity = getUser(userId);
        userEntity.setFirstName(userUpdationRequest.getFirstName());
        userEntity.setLastName(userUpdationRequest.getLastName());
        userEntity.setPassword(userUpdationRequest.getPassword());
        userEntity.setDob(userUpdationRequest.getDob());

        return userRepository.save(userEntity);
    }

    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "User has been deleted";
    }
}
