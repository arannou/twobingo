package com.twobingo.service;

import com.twobingo.model.Cell;
import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.CellRepository;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return gridRepository.findByCreator_id(creator);
    }

    public Grid createGrid(Grid grid) {
        return gridRepository.save(grid);
    }

    public Grid updateGrid(Long id, Grid gridDetails) {
        Optional<Grid> existingGrid = gridRepository.findById(id);
        if (existingGrid.isPresent()) {
            Grid grid = existingGrid.get();
            grid.setName(gridDetails.getName());
            grid.setDescription(gridDetails.getDescription());
            grid.setSize(gridDetails.getSize());
            grid.setStyle(gridDetails.getStyle());
            grid.setColor(gridDetails.getColor());
            grid.setIsArchived(gridDetails.getIsArchived());
            grid.setCreator_id(gridDetails.getCreator_id());
            return gridRepository.save(grid);
        }
        throw new RuntimeException("Grid not found with id: " + id);
    }

    public void deleteGrid(Long id) {
        gridRepository.deleteById(id);
    }

    public List<User> getUsersByGrid(Grid grid) {
        List<Participation> participations = participationRepository.findByGrid(grid);
        return participations.stream()
                .map(Participation::getUser)
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    public List<Grid> getGridsByUser(User user) {
        List<Participation> participations = participationRepository.findByUser(user);
        return participations.stream()
                .map(Participation::getGrid)
                .filter(gridObj -> gridObj != null)
                .collect(Collectors.toList());
    }

    public List<Cell> getCellsByGrid(Grid grid) {
        return cellRepository.findByGrid_Id(grid.getId());
    }
}
