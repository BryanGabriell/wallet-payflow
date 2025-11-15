package com.payflow.wallet.dto.user;


import com.payflow.wallet.dto.address.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Nome de usuário é obrigatório")
        @Size(min = 3, max = 100, message = "O nome de usuário deve ter entre 3 e 100 caracteres")
        String username,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        String phone,

        AddressRequest address,

        @NotBlank(message = "O papel (role) do usuário é obrigatório")
        String role) {
}
