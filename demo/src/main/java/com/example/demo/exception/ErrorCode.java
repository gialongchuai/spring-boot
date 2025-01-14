package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not exist!", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be at least 3 characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You don't have permission!", HttpStatus.FORBIDDEN);

    int code;
    String message;
    HttpStatusCode httpStatusCode;
}

