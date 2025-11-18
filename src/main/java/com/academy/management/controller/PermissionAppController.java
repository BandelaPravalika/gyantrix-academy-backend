package com.academy.management.controller;

import com.academy.management.model.PermissionApp;
import com.academy.management.service.PermissionAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
public class PermissionAppController {

    @Autowired
    private PermissionAppService service;

    @GetMapping
    public ResponseEntity<List<PermissionApp>> getAll() {
        try {
            return ResponseEntity.ok(service.getAllPermissions());
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionApp> getById(@PathVariable Long id) {
        try {
            PermissionApp p = service.getPermissionById(id);
            return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<PermissionApp> create(@RequestBody PermissionApp permission) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createPermission(permission));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionApp> update(@PathVariable Long id, @RequestBody PermissionApp permission) {
        try {
            PermissionApp updated = service.updatePermission(id, permission);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PermissionApp> patch(@PathVariable Long id, @RequestBody PermissionApp permission) {
        try {
            PermissionApp patched = service.patchPermission(id, permission);
            return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean deleted = service.deletePermission(id);
            if (deleted) {
                response.put("message", "Permission with ID " + id + " deleted successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Permission with ID " + id + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("message", "Error occurred while deleting permission: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
