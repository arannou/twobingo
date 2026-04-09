package com.twobingo.service;

import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import com.twobingo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    List<String> AVATARS = List.of(
            "butter.svg",
            "coral.svg",
            "lila.svg",
            "mint.svg",
            "peach.svg",
            "sky.svg");

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByConnectionToken(String token) {
        return userRepository.findByConnectionToken(token);
    }

    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with username: " + user.getUsername());
        }

        String token = UUID.randomUUID().toString();
        user.setConnectionToken(token); // created user is auto connected
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAvatarImagePath(getRandomAvatar());
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(userDetails.getUsername());
            user.setAvatarImagePath(userDetails.getAvatarImagePath());
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // delete many users by ids
    public void deleteUsers(List<Long> ids) {
        List<User> usersToDelete = userRepository.findAllById(ids);
        userRepository.deleteAll(usersToDelete);
    }

    public List<Grid> getCreatedGrids(User user) {
        return gridRepository.findByCreator(user);
    }

    public List<Grid> getParticipatingGrids(User user) {
        List<Participation> participations = participationRepository.findByUseraccount(user);
        return participations.stream()
                .map(Participation::getGrid)
                .filter(grid -> grid != null)
                .collect(Collectors.toList());
    }

    /** Find all users created more that 2 years ago with no participations */
    public List<User> getInactiveUsers() {
        java.time.LocalDateTime twoYearsAgo = java.time.LocalDateTime.now().minusYears(2);
        List<User> oldUsers = userRepository.findByCreatedAtBefore(twoYearsAgo);
        List<Participation> theirParticipations = participationRepository.findByUseraccountIn(oldUsers);
        // if no participations, then they are inactive
        return oldUsers.stream()
                .filter(user -> theirParticipations.stream().noneMatch(p -> p.getUser().equals(user)))
                .collect(Collectors.toList());

    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = getUserByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = UUID.randomUUID().toString();
                user.setConnectionToken(token);
                userRepository.save(user);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    // logout method to invalidate the token
    public void logout(String token) {
        Optional<User> userOpt = getUserByConnectionToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setConnectionToken(null);
            userRepository.save(user);
        }
    }

    // validateToken
    public Optional<User> validateToken(String token) {
        Optional<User> userOpt = getUserByConnectionToken(token);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (token.equals(user.getConnectionToken())) {
                return userOpt;
            } else {
                user.setConnectionToken(null);
                userRepository.save(user);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private String getRandomAvatar() {
        int index = (int) (Math.random() * AVATARS.size());
        return AVATARS.get(index);
    }
}
