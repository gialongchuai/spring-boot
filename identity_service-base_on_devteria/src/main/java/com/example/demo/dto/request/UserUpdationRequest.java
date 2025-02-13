package com.example.demo.dto.request;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Size;

import com.example.demo.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdationRequest {
    String firstName;
    String lastName;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;

    Set<String> roles;
}
