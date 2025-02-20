package com.banking.repository;

import com.banking.entity.Transaction;
import com.banking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.user.id = :userId ORDER BY t.timestamp DESC")
    List<Transaction> findByFromAccount_User(Long userId);
}
