package com.payflow.wallet.repository;

import com.payflow.wallet.entity.VirtualCard;
import com.payflow.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface VirtualCardRepository extends JpaRepository<VirtualCard,Long> {
    Optional<VirtualCard> findById(Long id);
}
