package com.example.demo.controller;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.enums.SuccessCode;
import com.example.demo.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

//    @PostMapping("/log-in")
//    public ApiResponse<AuthenticationResponse> sampleLogin(@RequestBody AuthenticationRequest authenticationRequest){
//        ApiResponse<AuthenticationResponse> authenticationResponseApiResponse = new ApiResponse<>();
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//        boolean result = authenticationService.login(authenticationRequest);
//        authenticationResponse.setResult(result);
//        authenticationResponseApiResponse.setResult(authenticationResponse);
//        return authenticationResponseApiResponse;
//    }

    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ApiResponse.<AuthenticationResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }
}
