package com.twobingo.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import com.twobingo.repository.GridRepository;
import com.twobingo.repository.ParticipationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipationService {

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    public List<User> getUsersByGrid(Grid grid) {
        List<Participation> participations = participationRepository.findByGrid(grid);
        return participations.stream()
                .map(Participation::getUser)
                .filter(user -> user != null)
                .collect(Collectors.toList());
    }

    public List<Grid> getGridsByUser(User user) {
        List<Participation> participations = participationRepository.findByUseraccount(user);
        return participations.stream()
                .map(Participation::getGrid)
                .filter(gridObj -> gridObj != null)
                .collect(Collectors.toList());
    }

    public Participation createParticipation(@NonNull Participation participation) {
        if (participation.getGrid() == null || participation.getUser() == null) {
            throw new IllegalArgumentException("Grid and User must not be null");
        }
        return participationRepository.save(participation);
    }
}
