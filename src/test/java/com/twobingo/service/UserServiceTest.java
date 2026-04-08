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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GridRepository gridRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByUsername_returnsUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("player1");

        when(userRepository.findByUserName("player1")).thenReturn(Optional.of(user));

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

        when(participationRepository.findByUser(user)).thenReturn(singletonList(participation));

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

        when(gridRepository.findByCreator_id(user)).thenReturn(singletonList(grid));

        List<Grid> result = userService.getCreatedGrids(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Created Grid");
    }
}
