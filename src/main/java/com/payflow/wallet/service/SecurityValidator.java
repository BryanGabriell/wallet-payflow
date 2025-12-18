package com.payflow.wallet.service;


import com.payflow.wallet.enums.userenums.Role;
import com.payflow.wallet.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class SecurityValidator {

    private final AuthService authService;

    public SecurityValidator(AuthService authService) {
        this.authService = authService;
    }

    public void validateUserOrAdmin(Long targetUserId) {

        Long loggedId = authService.getLoggedUserId();
        Role loggedRole = authService.getLoggedUserRoleEnum();

        boolean isSameUser = loggedId.equals(targetUserId);
        boolean isAdmin = loggedRole.equals(Role.ROLE_ADMIN);

        if (!isSameUser && !isAdmin) {
            throw new UnauthorizedException("Você não tem permissão para acessar este recurso.");
        }
    }

    public void validateAdmin() {
        if (!authService.getLoggedUserRoleEnum().equals(Role.ROLE_ADMIN)) {
            throw new UnauthorizedException("Acesso restrito a administradores.");
        }
    }

    public void validateSameUser(Long userId) {
        if (!authService.getLoggedUserId().equals(userId)) {
            throw new UnauthorizedException("Você não pode acessar dados de outro usuário.");
        }
    }

}
