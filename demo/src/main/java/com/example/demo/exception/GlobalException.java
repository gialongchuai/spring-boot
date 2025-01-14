package com.example.demo.exception;

import com.example.demo.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;

@ControllerAdvice
public class GlobalException {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> sampleHandlingRunTimeException(RuntimeException runtimeException){
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
//        apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRunTimeException(RuntimeException exception){
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build());
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException appException){
        ErrorCode errorCode = appException.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException accessDeniedException) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message((errorCode.getMessage()))
                .build());
    }

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> sampleHandlingMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
//        String enumKey = methodArgumentNotValidException.getFieldError().getDefaultMessage();
//        ErrorCode errorCode;
//
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//        } catch (Exception e){
//            errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
//        }
//
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
//    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        String enumKey = methodArgumentNotValidException.getFieldError().getDefaultMessage();
        ErrorCode errorCode;

        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (Exception e){
            errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        }

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .code((errorCode.getCode()))
                        .message(errorCode.getMessage())
                .build());
    }
}
