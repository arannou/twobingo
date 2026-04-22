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
    void getCellsByGrid_returnsCellsForGrid() {
        Grid grid = new Grid();
        grid.setId(3L);

        Cell cell = new Cell();
        cell.setId(5L);
        cell.setContent("Bingo Cell");

        when(cellRepository.findByGrid_Id(3L)).thenReturn(singletonList(cell));

        List<Cell> result = gridService.getCellsByGrid(grid);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("Bingo Cell");
    }

    @Test
    void updateGrid_updatesExistingGrid() {
        Grid existing = new Grid();
        existing.setId(1L);
        existing.setName("Old Name");
        existing.setDescription("Old desc");
        existing.setSize(3);
        existing.setTheme("old-style");
        existing.setColor("blue");
        existing.setIsArchived(false);

        Grid details = new Grid();
        details.setName("New Name");
        details.setDescription("New desc");
        details.setSize(5);
        details.setTheme("new-style");
        details.setColor("red");
        details.setIsArchived(true);

        when(gridRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(gridRepository.save(any(Grid.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Grid result = gridService.updateGrid(1L, details);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDescription()).isEqualTo("New desc");
        assertThat(result.getSize()).isEqualTo(5);
        assertThat(result.getTheme()).isEqualTo("new-style");
        assertThat(result.getColor()).isEqualTo("red");
        assertThat(result.getIsArchived()).isTrue();
    }

    @Test
    void deleteGrid_callsRepositoryDelete() {
        gridService.deleteGrid(2L);

        verify(gridRepository).deleteById(2L);
    }
}
