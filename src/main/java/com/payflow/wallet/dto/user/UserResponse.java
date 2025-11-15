package com.payflow.wallet.dto.user;


import com.payflow.wallet.dto.address.AddressResponse;

public record UserResponse(Long id,
                           String username,
                           String email,
                           String phone,
                           AddressResponse address,
                           String role) {
}
