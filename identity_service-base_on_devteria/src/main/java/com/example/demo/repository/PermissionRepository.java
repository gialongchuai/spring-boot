package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByName(String name);
}
