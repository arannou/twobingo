package com.twobingo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.twobingo.dto.CellDto;
import com.twobingo.dto.GridDto;
import com.twobingo.exceptions.AuthException;
import com.twobingo.exceptions.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.twobingo.model.Grid;
import com.twobingo.model.Cell;
import com.twobingo.model.User;
import com.twobingo.model.Participation;
import com.twobingo.service.GridService;
import com.twobingo.service.ParticipationService;
import com.twobingo.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/twobingo")
@CrossOrigin(origins = "*")
public class TwobingoController {

    @Autowired
    private GridService gridService;

    @Autowired
    private UserService userService;

    @Autowired
    private ParticipationService participationService;

    // User management endpoints CRUD operations for users, fetching user details,
    // etc.

    // @GetMapping("/users")
    // public ResponseEntity<List<User>> getAllUsers() {
    // return ResponseEntity.ok(userService.getAllUsers());
    // }

    // @GetMapping("/users/{id}")
    // public ResponseEntity<User> getUserById(@PathVariable Long id) {
    // return userService.getUserById(id)
    // .map(ResponseEntity::ok)
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // @GetMapping("/users/username/{username}")
    // public ResponseEntity<User> getUserByUsername(@PathVariable String username)
    // {
    // return userService.getUserByUsername(username)
    // .map(ResponseEntity::ok)
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @PostMapping("/users/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                Map<String, String> errorResponse = Map.of("error", "Username is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                Map<String, String> errorResponse = Map.of("error", "Password is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errorResponse);
            }
            Map<String, Object> successResponse = Map.of(
                    "username", user.getUsername(),
                    "avatarImagePath", user.getAvatarImagePath(),
                    "connectionToken", user.getConnectionToken());
            return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            System.err.println("Error creating user: " + ex.getMessage());
            ex.printStackTrace();
            Map<String, String> errorResponse = Map.of("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    // @PutMapping("/users/{id}")
    // public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody
    // User userDetails) {
    // try {
    // return ResponseEntity.ok(userService.updateUser(id, userDetails));
    // } catch (RuntimeException ex) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    // }
    // }

    // @DeleteMapping("/users/{id}")
    // public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    // userService.deleteUser(id);
    // return ResponseEntity.noContent().build();
    // }

    // @GetMapping("/users/{userId}/created-grids")
    // public ResponseEntity<List<Grid>> getCreatedGrids(@PathVariable Long userId)
    // {
    // return userService.getUserById(userId)
    // .map(user -> ResponseEntity.ok(userService.getCreatedGrids(user)))
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @GetMapping("/grids/user")
    public ResponseEntity<?> getParticipatingGrids(@RequestHeader("Authorization") String token) {
        try {
            User user = getValidUserFromToken(token);
            List<Grid> grid = userService.getParticipatingGrids(user);
            System.out.println("Participating grids for user " + user.getUsername() + ": " + grid.size());
            Map<String, Object> grids_map = Map.of("grids", grid
                    .stream()
                    .map(Grid::toResponseMap)
                    .toList());
            return ResponseEntity.ok(grids_map);
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    // Grid management endpoints CRUD operations for grids and cells, participation
    // management, etc.

    // @GetMapping("/grids")
    // public ResponseEntity<List<Grid>> getAllGrids(@RequestHeader("Authorization")
    // String token) {
    // return ResponseEntity.ok(gridService.getAllGrids());
    // }

    @GetMapping("/grids/{id}")
    public ResponseEntity<Grid> getGridById(@PathVariable Long id) {
        return gridService.getGridById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/grids")
    public ResponseEntity<?> createGrid(@RequestHeader("Authorization") String token, @RequestBody GridDto gridDto) {
        try {
            User user = getValidUserFromToken(token);

            // Convert GridDto to Grid
            Grid grid = new Grid();
            grid.setName(gridDto.getName());
            grid.setDescription(gridDto.getDescription());
            grid.setSize(gridDto.getSize());
            grid.setTheme(gridDto.getTheme());
            grid.setColor(gridDto.getColor());
            grid.setIsArchived(gridDto.getIsArchived());
            grid.setCreator(user);

            // Convert CellDto to Cell
            List<Cell> cells = new ArrayList<>();
            if (gridDto.getCells() != null) {
                for (CellDto cellDto : gridDto.getCells()) {
                    Cell cell = new Cell();
                    cell.setContent(cellDto.getContent());
                    cell.setIsChecked(cellDto.getChecked());
                    cell.setPosition(cellDto.getPosition());
                    cells.add(cell);
                }
            }
            grid.setCells(cells);

            // Create and validate the grid, then create a participation for the creator
            Grid createdGrid = gridService.createGrid(grid);
            Participation participation = new Participation(createdGrid, user,
                    user.getUsername(), true);
            participationService.createParticipation(participation);

            Map<String, Object> responseGrid = createdGrid.toResponseMap();

            return new ResponseEntity<>(responseGrid,
                    HttpStatus.CREATED);

        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

    // @GetMapping("/grids/{gridId}/cells")
    // public ResponseEntity<List<Cell>> getCellsByGrid(@PathVariable Long gridId) {
    // return gridService.getGridById(gridId)
    // .map(grid -> ResponseEntity.ok(gridService.getCellsByGrid(grid)))
    // .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    @GetMapping("/grids/{gridId}/users")
    public ResponseEntity<List<User>> getUsersByGrid(@PathVariable Long gridId) {
        return gridService.getGridById(gridId)
                .map(grid -> ResponseEntity.ok(participationService.getUsersByGrid(grid)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Map<String, Object> response = Map.of(
                    "username", user.getUsername(),
                    "avatarImagePath", user.getAvatarImagePath(),
                    "connectionToken", user.getConnectionToken());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = Map.of("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // logout endpoint to invalidate the token
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        Map<String, String> response = Map.of("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    // is token valid endpoint to check if the token is valid
    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            User user = getValidUserFromToken(token);
            Map<String, Object> response = Map.of(
                    "username", user.getUsername(),
                    "avatarImagePath", user.getAvatarImagePath());
            return ResponseEntity.ok(response);
        } catch (AuthException e) {
            Map<String, String> errorResponse = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    private User getValidUserFromToken(String token) throws AuthException {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Optional<User> userOpt = userService.validateToken(token);
        if (!userOpt.isPresent()) {
            throw new AuthException("Invalid token");
        }
        return userOpt.get();
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, String> response = Map.of("status", "UP", "message", "Twobingo Service is running");
        return ResponseEntity.ok(response);
    }
}
