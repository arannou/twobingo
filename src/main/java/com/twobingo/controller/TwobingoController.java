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

    @PostMapping("/users/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username is required"));
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Password is required"));
            }
            return new ResponseEntity<>(Map.of("username", user.getUsername(), "avatarImagePath",
                    user.getAvatarImagePath(), "connectionToken", user.getConnectionToken()), HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            System.err.println("Error creating user: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
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
    public ResponseEntity<List<Grid>> getAllGrids(@RequestHeader("Authorization") String token) {
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

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        System.out.println("Login attempt for username: " + loginRequest.getUsername());
        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(Map.of("username", user.getUsername(), "avatarImagePath",
                    user.getAvatarImagePath(), "connectionToken", user.getConnectionToken()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    // logout endpoint to invalidate the token
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    // is token valid endpoint to check if the token is valid
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        Optional<User> userOpt = userService.validateToken(token);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        User user = userOpt.get();
        return ResponseEntity.ok(Map.of("username", user.getUsername(), "avatarImagePath", user.getAvatarImagePath()));
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Twobingo Service is running");
    }
}
