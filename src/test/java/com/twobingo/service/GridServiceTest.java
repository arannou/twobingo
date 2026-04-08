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

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GridServiceTest {

    @Mock
    private GridRepository gridRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private CellRepository cellRepository;

    @InjectMocks
    private GridService gridService;

    @Test
    void getAllGrids_returnsAllGrids() {
        Grid grid = new Grid();
        grid.setId(1L);
        grid.setName("Test Grid");

        when(gridRepository.findAll()).thenReturn(singletonList(grid));

        List<Grid> result = gridService.getAllGrids();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Grid");
    }

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

        List<User> result = gridService.getUsersByGrid(grid);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUsername()).isEqualTo("player1");
    }

    @Test
    void getCellsByGrid_returnsCellsForGrid() {
        Grid grid = new Grid();
        grid.setId(3L);

        Cell cell = new Cell();
        cell.setId(5L);
        cell.setContent("Bingo Cell");

        when(cellRepository.findByGrid_id(3L)).thenReturn(singletonList(cell));

        List<Cell> result = gridService.getCellsByGrid(grid);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("Bingo Cell");
    }
}
