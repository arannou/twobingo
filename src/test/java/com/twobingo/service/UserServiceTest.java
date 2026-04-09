package com.twobingo.service;

import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import com.twobingo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GridRepository gridRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByUsername_returnsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("player1");

        when(userRepository.findByUsername("player1")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("player1");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("player1");
    }

    @Test
    void getParticipatingGrids_returnsGridsForUser() {
        User user = new User();
        user.setId(2L);
        user.setUsername("player2");

        Grid grid = new Grid();
        grid.setId(10L);
        grid.setName("Participation Grid");

        Participation participation = new Participation();
        participation.setUser(user);
        participation.setGrid(grid);

        when(participationRepository.findByUseraccount(user)).thenReturn(singletonList(participation));

        List<Grid> result = userService.getParticipatingGrids(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Participation Grid");
    }

    @Test
    void getCreatedGrids_returnsGridsForUser() {
        User user = new User();
        user.setId(3L);
        user.setUsername("creator");

        Grid grid = new Grid();
        grid.setId(11L);
        grid.setName("Created Grid");

        when(gridRepository.findByCreator(user)).thenReturn(singletonList(grid));

        List<Grid> result = userService.getCreatedGrids(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Created Grid");
    }

    @Test
    void updateUser_updatesExistingUser() {
        User existing = new User();
        existing.setId(1L);
        existing.setUsername("oldUser");
        existing.setAvatarImagePath("/avatar.png");
        existing.setPassword("oldpass");

        User details = new User();
        details.setUsername("newUser");
        details.setAvatarImagePath("/new-avatar.png");
        details.setPassword("newpass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, details);

        assertThat(result.getUsername()).isEqualTo("newUser");
        assertThat(result.getAvatarImagePath()).isEqualTo("/new-avatar.png");
        assertThat(result.getPassword()).isEqualTo("newpass");
    }

    @Test
    void deleteUser_callsRepositoryDelete() {
        userService.deleteUser(4L);

        verify(userRepository).deleteById(4L);
    }

    @Test
    void login_returnsTokenForValidCredentials() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("hashedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<User> result = userService.login("testuser", "password");

        assertThat(result).isPresent();
        verify(userRepository).save(user);
    }

    @Test
    void login_returnsEmptyForInvalidCredentials() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("testuser", "password");

        assertThat(result).isEmpty();
    }
}
