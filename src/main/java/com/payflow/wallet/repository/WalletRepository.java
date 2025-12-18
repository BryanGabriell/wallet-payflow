package com.payflow.wallet.repository;

import com.payflow.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
   Optional<Wallet>findById(Long id);
    Optional<Wallet>findByUserId(Long userId);
}
