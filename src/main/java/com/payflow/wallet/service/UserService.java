package com.payflow.wallet.service;

import com.payflow.wallet.dto.address.AddressResponse;
import com.payflow.wallet.dto.user.UserRequest;
import com.payflow.wallet.dto.user.UserResponse;
import com.payflow.wallet.entity.Address;
import com.payflow.wallet.entity.User;
import com.payflow.wallet.entity.VirtualCard;
import com.payflow.wallet.entity.Wallet;
import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;
import com.payflow.wallet.enums.userenums.Role;
import com.payflow.wallet.enums.walletenums.WalletStatus;
import com.payflow.wallet.exception.CpfAlteracaoProibidaException;
import com.payflow.wallet.exception.EmailAlreadyUsedException;
import com.payflow.wallet.exception.UnauthorizedException;
import com.payflow.wallet.exception.UsuarioInexistenteError;
import com.payflow.wallet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final VirtualCardService virtualCardService;
    private final AuthService authService;


    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, VirtualCardService virtualCardService, AuthService authService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.virtualCardService = virtualCardService;
        this.authService = authService;
    }

    @Transactional
    public UserResponse registerUser(UserRequest dto) {
        repository.findByEmail(dto.email()).ifPresent(user -> {
            throw new EmailAlreadyUsedException("Email já está em uso");
        });
        String hashedPassword = passwordEncoder.encode(dto.password());

        Address address = Address.builder()
                .street(dto.address().street())
                .city(dto.address().city())
                .state(dto.address().state())
                .zipCode(dto.address().zipCode())
                .build();


        User user = User.builder().username(dto.username())
                .cpf(dto.cpf())
                .password(hashedPassword).
                email(dto.email()).
                phone(dto.phone()).role(Role.ROLE_USER).
                address(address).
                build();

        address.setUser(user);

        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ATIVA)
                .user(user)
                .build();

        String generatedNumber = virtualCardService.generateRandomCardNumber();
        VirtualCard virtualCard = VirtualCard.builder()
                .cardNumber(generatedNumber)
                .cardLimit(BigDecimal.ZERO)
                .cardStatus(VirtualCardStatus.BLOQUEADO)
                .user(user)
                .build();

        user.setWallet(wallet);
        user.setVirtualCard(virtualCard);
        User savedUser = repository.save(user);

        AddressResponse addressResponse = new AddressResponse(
                savedUser.getId(),
                savedUser.getAddress().getStreet(),
                savedUser.getAddress().getCity(),
                savedUser.getAddress().getState(),
                savedUser.getAddress().getZipCode()
        );

        UserResponse response = new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCpf(),
                savedUser.getPhone(),
                addressResponse,
                savedUser.getRole().name()
        );

        return response;


    }

    @Transactional
    public UserResponse updateUser(Long userId, UserRequest dto, AddressResponse addressResponse) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UsuarioInexistenteError("Usuário não existe"));

        Long loggedId = authService.getLoggedUserId();
        Role loggedRole = authService.getLoggedUserRoleEnum();

        boolean isSameUser = loggedId.equals(userId);
        boolean isAdmin = loggedRole.equals(Role.ROLE_ADMIN);

        if (!isSameUser && !isAdmin) {
            throw new UnauthorizedException("Você não tem permissão para atualizar este usuário.");
        }

        if (!user.getEmail().equals(dto.email())) {
            repository.findByEmail(dto.email()).ifPresent(email -> {
                throw new EmailAlreadyUsedException("Email já está em uso.");
            });
        }

        if (!user.getCpf().equals(dto.cpf())) {
            throw new CpfAlteracaoProibidaException("CPF não pode ser alterado.");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());

        User saved = repository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCpf(),
                saved.getPhone(),
                addressResponse,
                saved.getRole().name()
        );

    }
}