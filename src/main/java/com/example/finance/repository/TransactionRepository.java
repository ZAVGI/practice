package com.example.finance.repository;

import com.example.finance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUserUsername(String username);

    Optional<Transaction> findByIdAndUserUsername(Long id, String username);

    List<Transaction> findAllByUserUsernameAndTimestampBetween(String username, LocalDateTime start, LocalDateTime end);
}
