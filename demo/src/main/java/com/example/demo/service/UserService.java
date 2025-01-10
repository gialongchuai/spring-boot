package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
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

        UserEntity userEntity = userMapper.toUserEntity(userCreationRequest);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public List<UserResponse> getAllUser(){
        return userMapper.toUsersResponse(userRepository.findAll());
    }

    public UserResponse getUser(String userId){
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!")));
    }

    public UserResponse updateUser(String userId, UserUpdationRequest userUpdationRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        userMapper.updateUser(userEntity, userUpdationRequest);
        return  userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "User has been deleted";
    }
}
