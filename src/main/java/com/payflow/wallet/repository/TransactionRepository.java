package com.payflow.wallet.repository;

import com.payflow.wallet.entity.Transaction;
import com.payflow.wallet.enums.transactionsenums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findFirstBySender_User_IdAndStatus(Long userId, TransactionStatus status);
    List<Transaction> findBySender_User_IdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByReceiver_User_IdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    @Query("SELECT t FROM Transaction t WHERE (t.sender.user.id = :userId OR t.receiver.user.id = :userId) AND t.createdAt BETWEEN :start AND :end")
    List<Transaction> findAllByUserIdAndCreatedAtBetween(@Param("userId") Long userId,
                                                         @Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);
}
