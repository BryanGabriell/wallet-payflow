package com.payflow.wallet.dto.user;


import com.payflow.wallet.dto.address.AddressRequest;
import com.payflow.wallet.enums.userenums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record UserRequest(
        @NotBlank(message = "Nome de usuário é obrigatório")
        @Size(min = 3, max = 100, message = "O nome de usuário deve ter entre 3 e 100 caracteres")
        String username,

        @CPF(message = "CPF fornecido não é válido")
        String cpf,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        String password,

        @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
        String phone,

        @Valid AddressRequest address,

        @NotNull(message = "O papel do usuário é obrigatório")
        Role role) {
}
