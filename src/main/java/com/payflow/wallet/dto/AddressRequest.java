package com.payflow.wallet.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "Rua é obrigatória") String street,
        @NotBlank(message = "Cidade é obrigatória") String city,
        @NotBlank(message = "Estado é obrigatório") String state,
        @NotBlank(message = "CEP é obrigatório") String zipCode
) {
}
