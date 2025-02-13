package com.example.demo.dto.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String description;

    // toang cuc nang
    Set<PermissionResponse> permissions;
}
