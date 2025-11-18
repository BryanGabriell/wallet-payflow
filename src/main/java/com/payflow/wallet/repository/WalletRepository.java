package com.payflow.wallet.repository;

import com.payflow.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
