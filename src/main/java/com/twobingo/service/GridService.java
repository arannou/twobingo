package com.twobingo.service;

import com.twobingo.exceptions.ValidationException;
import com.twobingo.model.Cell;
import com.twobingo.model.Grid;
import com.twobingo.model.User;
import com.twobingo.repository.CellRepository;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GridService {

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private CellRepository cellRepository;

    public List<Grid> getAllGrids() {
        return gridRepository.findAll();
    }

    public Optional<Grid> getGridById(Long id) {
        return gridRepository.findById(id);
    }

    public List<Grid> getGridsByCreator(User creator) {
        return gridRepository.findByCreator(creator);
    }

    public Grid createGrid(Grid grid) throws ValidationException {
        if (grid.getName() == null || grid.getName().isEmpty()) {
            throw new ValidationException("Grid name cannot be empty");
        }
        if (grid.getSize() == null || grid.getSize() <= 0) {
            throw new ValidationException("Grid size must be a positive integer");
        }
        if (grid.getCells() != null && grid.getCells().size() < 1) {
            throw new ValidationException("Number of cells cannot be less than 1");
        }
        if (grid.getCells() != null && grid.getCells().size() > grid.getSize() * grid.getSize()) {
            throw new ValidationException("Number of cells cannot exceed grid size squared");
        }
        // for each cell in grid.cells, create a new Cell object and set its grid
        // reference to the created grid
        List<Cell> createdCells = new ArrayList<>();
        if (grid.getCells() != null) {
            for (Cell cell : grid.getCells()) {
                if (cell == null)
                    continue;
                createdCells.add(cellRepository.save(cell));
            }
        }
        Grid createdGrid = gridRepository.save(grid);
        for (Cell cell : createdCells) {
            cell.setGrid(createdGrid);
            cellRepository.save(cell);
        }
        return createdGrid;
    }

    public Grid updateGrid(@NonNull Long id, Grid gridDetails) {
        Optional<Grid> existingGrid = gridRepository.findById(id);
        if (existingGrid.isPresent()) {
            Grid grid = existingGrid.get();
            grid.setName(gridDetails.getName());
            grid.setDescription(gridDetails.getDescription());
            grid.setSize(gridDetails.getSize());
            grid.setTheme(gridDetails.getTheme());
            grid.setColor(gridDetails.getColor());
            grid.setIsArchived(gridDetails.getIsArchived());
            grid.setCreator(gridDetails.getCreator());
            Grid updatedGrid = gridRepository.save(grid);
            for (Cell cellDetails : gridDetails.getCells()) {
                Long cellId = cellDetails.getId();
                Cell cell = new Cell();
                if (cellId != null) {
                    Optional<Cell> cellOpt = cellRepository.findById(cellId);
                    if (cellOpt.isPresent()) {
                        cell = cellOpt.get();
                    }
                }
                cell.setContent(cellDetails.getContent());
                cell.setIsChecked(cellDetails.getIsChecked());
                cell.setPosition(cellDetails.getPosition());
                cell.setGrid(updatedGrid);

                cellRepository.save(cell);

            }
            return updatedGrid;
        }
        throw new RuntimeException("Grid not found with id: " + id);
    }

    public void deleteGrid(Long id) {
        gridRepository.deleteById(id);
    }

    public List<Cell> getCellsByGrid(Grid grid) {
        return cellRepository.findByGrid_Id(grid.getId());
    }
}
