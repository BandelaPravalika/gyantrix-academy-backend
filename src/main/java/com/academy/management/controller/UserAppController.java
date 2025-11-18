package com.academy.management.controller;

import com.academy.management.model.UserApp;
import com.academy.management.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserAppController {

    @Autowired
    private UserAppService userAppService;

    @GetMapping
    public ResponseEntity<List<UserApp>> getAllUsers() {
        return ResponseEntity.ok(userAppService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserApp> getUserById(@PathVariable Long id) {
        UserApp user = userAppService.getUserById(id);
        if (user != null) return ResponseEntity.ok(user);
        else return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UserApp> createUser(@RequestBody UserApp user) {
        UserApp createdUser = userAppService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserApp> updateUser(@PathVariable Long id, @RequestBody UserApp user) {
        user.setId(id);
        userAppService.updateUser(user);
        return ResponseEntity.ok(userAppService.getUserById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserApp> updateUserPartial(@PathVariable Long id, @RequestBody UserApp user) {
        user.setId(id);
        userAppService.updateUserPartial(user);
        return ResponseEntity.ok(userAppService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userAppService.deleteUser(id);
        if (deleted) return ResponseEntity.ok("User deleted successfully");
        else return ResponseEntity.status(404).body("User not found");
    }
}
