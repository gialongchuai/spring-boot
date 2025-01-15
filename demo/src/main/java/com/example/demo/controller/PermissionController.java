package com.example.demo.controller;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.entity.Permission;
import com.example.demo.enums.SuccessCode;
import com.example.demo.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping("/create")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(permissionRequest))
                .build();
    }

    @GetMapping()
    List<PermissionResponse> getPermissions() {
        return permissionService.getPermissions().stream().toList();
    }

    @DeleteMapping("/{permissionName}")
    ApiResponse<PermissionResponse> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ApiResponse.<PermissionResponse>builder()
                .code(SuccessCode.SUCCESS_CODE.getCode())
                .message("Delete permission successfully!")
                .build();
    }

    @PutMapping("/{permissionName}")
    ApiResponse<PermissionResponse> updatePermission(@PathVariable String permissionName, @RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.updatePermission(permissionName, permissionRequest))
                .build();
    }
}
