package com.example.demo.dto.response;

import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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
