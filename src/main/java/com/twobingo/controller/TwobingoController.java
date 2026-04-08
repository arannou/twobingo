package com.twobingo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.twobingo.controller.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.twobingo.model.Grid;
import com.twobingo.model.Cell;
import com.twobingo.model.User;
import com.twobingo.model.Participation;
import com.twobingo.service.GridService;
import com.twobingo.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/twobingo")
@CrossOrigin(origins = "*")
public class TwobingoController {

    @Autowired
    private GridService gridService;

    @Autowired
    private UserService userService;

    // User management endpoints CRUD operations for users, fetching user details,
    // etc.

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDetails));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/created-grids")
    public ResponseEntity<List<Grid>> getCreatedGrids(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(userService.getCreatedGrids(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}/participating-grids")
    public ResponseEntity<List<Grid>> getParticipatingGrids(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(userService.getParticipatingGrids(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Grid management endpoints CRUD operations for grids and cells, participation
    // management, etc.

    @GetMapping("/grids")
    public ResponseEntity<List<Grid>> getAllGrids() {
        return ResponseEntity.ok(gridService.getAllGrids());
    }

    @GetMapping("/grids/{id}")
    public ResponseEntity<Grid> getGridById(@PathVariable Long id) {
        return gridService.getGridById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/grids")
    public ResponseEntity<Grid> createGrid(@RequestBody Grid grid) {
        return new ResponseEntity<>(gridService.createGrid(grid), HttpStatus.CREATED);
    }

    @PutMapping("/grids/{id}")
    public ResponseEntity<Grid> updateGrid(@PathVariable Long id, @RequestBody Grid gridDetails) {
        try {
            return ResponseEntity.ok(gridService.updateGrid(id, gridDetails));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/grids/{id}")
    public ResponseEntity<Void> deleteGrid(@PathVariable Long id) {
        gridService.deleteGrid(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grids/{gridId}/cells")
    public ResponseEntity<List<Cell>> getCellsByGrid(@PathVariable Long gridId) {
        return gridService.getGridById(gridId)
                .map(grid -> ResponseEntity.ok(gridService.getCellsByGrid(grid)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/grids/{gridId}/users")
    public ResponseEntity<List<User>> getUsersByGrid(@PathVariable Long gridId) {
        return gridService.getGridById(gridId)
                .map(grid -> ResponseEntity.ok(gridService.getUsersByGrid(grid)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<String> token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (token.isPresent()) {
            return ResponseEntity.ok(Map.of("connectionToken", token.get()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // logout endpoint to invalidate the token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String username) {
        userService.logout(username);
        return ResponseEntity.ok("Logged out successfully");
    }

    // is token valid endpoint to check if the token is valid
    @GetMapping("/validate-token/{username}")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token,
            @PathVariable String username) {
        boolean isValid = userService.validateToken(username, token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        return ResponseEntity.ok("Token is valid");
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Twobingo Service is running");
    }
}
