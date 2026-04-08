package com.twobingo.service;

import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import com.twobingo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public User createUser(User user) {
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
        return gridRepository.findByCreator_id(user);
    }

    public List<Grid> getParticipatingGrids(User user) {
        List<Participation> participations = participationRepository.findByUser(user);
        return participations.stream()
                .map(Participation::getGrid)
                .filter(grid -> grid != null)
                .collect(Collectors.toList());
    }

    /** Find all users created more that 2 years ago with no participations */
    public List<User> getInactiveUsers() {
        java.time.LocalDateTime twoYearsAgo = java.time.LocalDateTime.now().minusYears(2);
        List<User> oldUsers = userRepository.findByCreatedDateBefore(twoYearsAgo);
        List<Participation> theirParticipations = participationRepository.findByUserIn(oldUsers);
        // if no participations, then they are inactive
        return oldUsers.stream()
                .filter(user -> theirParticipations.stream().noneMatch(p -> p.getUser().equals(user)))
                .collect(Collectors.toList());

    }
}
