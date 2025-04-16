package com.springboot_aws_dynamodb.controller;

import com.springboot_aws_dynamodb.model.User;
import com.springboot_aws_dynamodb.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "UserController")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "401", ref = "UnauthorizedResponse")
})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Creating new user with ID: {}", user.getUserId());
        try {
            User savedUser = userService.save(user);
            log.info("User created successfully with ID: {}", savedUser.getUserId());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            log.error("Error while creating user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        log.info("Fetching user with ID: {}", userId);
        Optional<User> user = userService.get(userId);
        return user.map(u -> {
            log.info("User found: {}", u);
            return ResponseEntity.ok(u);
        }).orElseGet(() -> {
            log.warn("User not found with ID: {}", userId);
            return ResponseEntity.notFound().build();
        });
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        log.info("Total users found: {}", users.size());
        return ResponseEntity.ok(users);
    }

    // Update user by ID
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        log.info("Updating user with ID: {}", userId);
        Optional<User> existingUser = userService.get(userId);
        if (existingUser.isPresent()) {
            user.setUserId(userId);
            try {
                User updatedUser = userService.save(user);
                log.info("User updated successfully with ID: {}", updatedUser.getUserId());
                return ResponseEntity.ok(updatedUser);
            } catch (Exception e) {
                log.error("Error while updating user: {}", e.getMessage(), e);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            log.warn("User not found for update with ID: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Deleting user with ID: {}", userId);
        try {
            userService.delete(userId);
            log.info("User deleted with ID: {}", userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error while deleting user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}