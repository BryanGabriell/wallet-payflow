package com.payflow.wallet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "virtual_cards")
public class VirtualCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cardLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VirtualCardStatus cardStatus;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference("user-virtualcard")
    private User user;
}
