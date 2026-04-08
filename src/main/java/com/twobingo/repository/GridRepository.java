package com.twobingo.repository;

import com.twobingo.model.Grid;
import com.twobingo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GridRepository extends JpaRepository<Grid, Long> {

    List<Grid> findByCreator(User creator);

}