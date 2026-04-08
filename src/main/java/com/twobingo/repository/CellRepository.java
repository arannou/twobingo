package com.twobingo.repository;

import com.twobingo.model.Cell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CellRepository extends JpaRepository<Cell, Long> {

    List<Cell> findByGrid_Id(Long gridId);

}