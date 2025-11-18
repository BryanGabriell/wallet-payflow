package com.payflow.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.payflow.wallet.enums.userenums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @JsonManagedReference("user-address")
    private Address address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-wallet")
    private Wallet wallet;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-virtualcard")
    private VirtualCard virtualCard;

}
