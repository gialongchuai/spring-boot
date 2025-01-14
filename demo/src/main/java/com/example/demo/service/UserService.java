package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.Role;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse addUser(UserCreationRequest userCreationRequest){
        if(userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserEntity userEntity = userMapper.toUserEntity(userCreationRequest);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        userEntity.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUser(){
        log.info("I am joining in method (getAllUser)!");
        return userMapper.toUsersResponse(userRepository.findAll());
    }

    @PostAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String userId){
        log.info("I am joining in method (getUser)!");
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    // chỉ cần kèm theo 1 token thì sẽ cho biết info người gửi thông qua token
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        var name = context.getAuthentication().getName();
        return userMapper.toUserResponse(userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse updateUser(String userId, UserUpdationRequest userUpdationRequest) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(userEntity, userUpdationRequest);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return  userMapper.toUserResponse(userRepository.save(userEntity));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
