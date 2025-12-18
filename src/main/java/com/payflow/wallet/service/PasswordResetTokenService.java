package com.payflow.wallet.service;

import com.payflow.wallet.entity.PasswordResetToken;
import com.payflow.wallet.repository.PasswordResetTokenRepository;
import com.payflow.wallet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository repository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Value("${app.frontend.reset-password-url}")
    private String resetPasswordUrl;

    @Transactional
    public void generateAndSendToken(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            repository.deleteByUser(user);

            LocalDateTime now = LocalDateTime.now();

            PasswordResetToken token = PasswordResetToken.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user)
                    .createdAt(now)
                    .expiresAt(now.plusMinutes(15))
                    .used(false)
                    .build();

            repository.save(token);

            String link = UriComponentsBuilder
                    .fromHttpUrl(resetPasswordUrl)
                    .queryParam("token", token.getToken())
                    .toUriString();

            String emailBody = """
                    Você solicitou a redefinição de senha.

                    Clique no link abaixo (válido por 15 minutos):
                    %s

                    Se não foi você, ignore este email.
                    """.formatted(link);

            emailService.send(
                    user.getEmail(),
                    "Recuperação de senha - PayFlow",
                    emailBody
            );
        });
    }


    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = validateToken(token);

        userService.updatePassword(
                resetToken.getUser().getId(),
                newPassword
        );

        resetToken.setUsed(true);
        repository.save(resetToken);
    }

    public PasswordResetToken validateToken(String token) {

        PasswordResetToken resetToken = repository
                .findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            resetToken.setUsed(true);
            repository.save(resetToken);
            throw new RuntimeException("Token expirado");
        }

        return resetToken;
    }
}
