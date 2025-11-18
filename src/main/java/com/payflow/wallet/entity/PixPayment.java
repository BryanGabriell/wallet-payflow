package com.payflow.wallet.entity;

import com.payflow.wallet.enums.pixpaygamentenum.PixPaygamentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pix_payments")
public class PixPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;


    @Column(nullable = false, length = 120)
    private String pixKey;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String qrCodePayload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PixPaygamentStatus status;


    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;


    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    @Column(length = 200)
    private String cancellationReason;
}
