package com.payflow.wallet.dto;

public record AddressResponse(
        Long id,
        String street,
        String city,
        String state,
        String zipCode
) {
}
