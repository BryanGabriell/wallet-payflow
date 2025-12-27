package com.payflow.wallet.controller;


import com.payflow.wallet.dto.user.UserRequest;
import com.payflow.wallet.dto.user.UserResponse;
import com.payflow.wallet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users",
        description = "Operações relacionadas ao gerenciamento de usuários, incluindo cadastro, autenticação, atualização de dados e consulta de perfil.")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Realiza o cadastro de um novo usuário no sistema. "
                    + "Durante o processo, são criados automaticamente o usuário, "
                    + "sua carteira digital inicial e os dados básicos associados."
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest userRequest){
        UserResponse userResponse = userService.registerUser(userRequest);
        return ResponseEntity.status(200).body(userResponse);
    }

    @Operation(
            summary = "Atualizar dados do usuário",
            description = "Permite a atualização dos dados cadastrais do usuário, "
                    + "como nome, telefone e endereço. "
                    + "Apenas usuários autenticados podem atualizar seus próprios dados."
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest userRequest, @PathVariable Long userId){
        UserResponse userResponse = userService.updateUser(userId,userRequest);
        return ResponseEntity.status(200).body(userResponse);
    }

    @Operation(
            summary = "Excluir usuário",
            description = "Remove o usuário do sistema de forma definitiva. "
                    + "Essa operação também remove ou invalida dados associados, "
                    + "como carteira, cartões e transações pendentes."
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "Atualizar senha do usuário",
            description = "Permite a alteração da senha do usuário autenticado. "
                    + "A nova senha será armazenada de forma segura utilizando criptografia."
    )
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        userService.updatePassword(id, request.password());

        return ResponseEntity.status(204).build();
    }
}
