package com.payflow.wallet.dto.user;


import com.payflow.wallet.dto.address.AddressResponse;
import com.payflow.wallet.enums.userenums.Role;

public record UserResponse(Long id,
                           String username,
                           String email,
                           String cpf,
                           String phone,
                           AddressResponse address,
                           String role) {

    }

