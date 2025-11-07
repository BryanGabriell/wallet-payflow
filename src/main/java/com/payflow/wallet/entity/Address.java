package com.payflow.wallet.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String street;
    @Column(nullable = false, length = 80)
    private String city;
    @Column(nullable = false, length = 60)
    private String state;
    @Column(nullable = false, length = 15)
    private String zipCode;


    @OneToOne(mappedBy = "address")
    private User user;
}
