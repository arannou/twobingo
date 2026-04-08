package com.twobingo.repository;

import com.twobingo.model.Grid;
import com.twobingo.model.Participation;
import com.twobingo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> findByGrid(Grid grid);

    List<Participation> findByUser(User user);

    List<Participation> findByUserIn(List<User> oldUsers);

}