package com.example.demo.exception;

import com.example.demo.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> sampleHandlingRunTimeException(Exception exception){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

//    @ExceptionHandler(value = RuntimeException.class)
//    ResponseEntity<ApiResponse> handlingRunTimeException(RuntimeException runtimeException){
//        return ResponseEntity.badRequest().body(ApiResponse.builder()
//                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
//                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
//                .build());
//    }

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

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException){
        String enumKey = methodArgumentNotValidException.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constraintViolation = methodArgumentNotValidException.getBindingResult().getAllErrors().get(0)
                    .unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());
        } catch (Exception e){

        }

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .code((errorCode.getCode()))
                        .message(Objects.nonNull(attributes) ?
                                mapAttribute(errorCode.getMessage(), attributes)
                                : errorCode.getMessage())
                .build());
    }

    private String mapAttribute(String message, Map<String,Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
