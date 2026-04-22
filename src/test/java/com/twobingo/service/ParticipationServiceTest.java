package com.twobingo.service;

import com.twobingo.model.Cell;
import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.CellRepository;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceTest {

    @Mock
    private GridRepository gridRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private CellRepository cellRepository;

    @InjectMocks
    private ParticipationService participationService;

    @Test
    void getUsersByGrid_returnsUsersFromParticipation() {
        Grid grid = new Grid();
        grid.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setUsername("player1");

        Participation participation = new Participation();
        participation.setGrid(grid);
        participation.setUser(user);

        when(participationRepository.findByGrid(grid)).thenReturn(singletonList(participation));

        List<User> result = participationService.getUsersByGrid(grid);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("player1");
    }

}
