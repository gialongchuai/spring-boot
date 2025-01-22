package com.example.demo.controller;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntroSpectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.request.RefreshTokenRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.AuthenticationResponse;
import com.example.demo.dto.response.IntroSpectResponse;
import com.example.demo.dto.response.LogoutResponse;
import com.example.demo.enums.SuccessCode;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

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

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ApiResponse.<AuthenticationResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(authenticationService.authenticate(authenticationRequest))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntroSpectResponse> introspect(@RequestBody IntroSpectRequest introSpectRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<IntroSpectResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(authenticationService.introspect(introSpectRequest))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .message(SuccessCode.SUCCESS_CODE.getMessage())
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .result(authenticationService.refreshToken(request))
                .build();
    }
}
