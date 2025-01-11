package com.example.demo.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SuccessCode {
    SUCCESS_CODE(1000, "Deleted Successfully!");

    int code;
    String message;
}
