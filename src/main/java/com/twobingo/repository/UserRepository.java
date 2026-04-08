package com.twobingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twobingo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find an inventory item by SKU
     */
    Optional<User> findByUserName(String userName);

    /** Find old users */
    List<User> findByCreatedDateBefore(java.time.LocalDateTime date);

}
