package com.payflow.wallet.repository;

import com.payflow.wallet.entity.VirtualCard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VirtualCardRepository extends JpaRepository<VirtualCard,Long> {
}
