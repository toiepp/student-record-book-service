package com.mikholskiy.recordbook.repository;

import com.mikholskiy.recordbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByIsApprovedFalseAndCreatedDateBefore(LocalDateTime minusDays);
}