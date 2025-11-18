package com.payflow.wallet.repository;

import com.payflow.wallet.entity.PixPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PixPaymentRepository extends JpaRepository<PixPayment, Long> {
}
