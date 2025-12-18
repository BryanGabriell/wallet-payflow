package com.payflow.wallet.service;

import com.payflow.wallet.dto.address.AddressResponse;
import com.payflow.wallet.dto.user.UserRequest;
import com.payflow.wallet.dto.user.UserResponse;
import com.payflow.wallet.entity.*;
import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;
import com.payflow.wallet.enums.transactionsenums.TransactionStatus;
import com.payflow.wallet.enums.userenums.Role;
import com.payflow.wallet.exception.*;
import com.payflow.wallet.repository.RefreshTokenRepository;
import com.payflow.wallet.repository.TransactionRepository;
import com.payflow.wallet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VirtualCardService virtualCardService;
    private final AuthService authService;
    private final WalletService walletService;
    private final SecurityValidator securityValidator;
    private final TransactionRepository transactionRepository;
    private final RefreshTokenRepository refreshTokenRepository;



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, VirtualCardService virtualCardService, AuthService authService, WalletService walletService, SecurityValidator securityValidator, TransactionRepository transactionRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.virtualCardService = virtualCardService;
        this.authService = authService;
        this.walletService = walletService;
        this.securityValidator = securityValidator;
        this.transactionRepository = transactionRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public UserResponse registerUser(UserRequest dto) {
        userRepository.findByEmail(dto.email()).ifPresent(user -> {
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

        Wallet wallet = walletService.createWallet(user);



        String generatedNumber = virtualCardService.generateRandomCardNumber();
        VirtualCard virtualCard = VirtualCard.builder()
                .cardNumber(generatedNumber)
                .cardLimit(BigDecimal.ZERO)
                .cardStatus(VirtualCardStatus.BLOQUEADO)
                .user(user)
                .build();

        user.setWallet(wallet);
        user.setVirtualCard(virtualCard);
        User savedUser = userRepository.save(user);

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
    public UserResponse updateUser(Long userId, UserRequest dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioInexistenteError("Usuário não existe"));

        securityValidator.validateUserOrAdmin(userId);

        if (!user.getEmail().equals(dto.email())) {
            userRepository.findByEmail(dto.email()).ifPresent(email -> {
                throw new EmailAlreadyUsedException("Email já está em uso.");
            });
        }

        if (!user.getCpf().equals(dto.cpf())) {
            throw new CpfAlteracaoProibidaException("CPF não pode ser alterado.");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());

        User saved = userRepository.save(user);

        AddressResponse address = new AddressResponse(
                saved.getId(),
                saved.getAddress().getStreet(),
                saved.getAddress().getCity(),
                saved.getAddress().getState(),
                saved.getAddress().getZipCode()
        );

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCpf(),
                saved.getPhone(),
                address,
                saved.getRole().name()
        );

    }
    @Transactional
    public void deleteUser(Long id){
        securityValidator.validateUserOrAdmin(id);
      User user =  userRepository.findById(id).orElseThrow(() -> new UsuarioInexistenteError("Usuario não existe"));
        Wallet wallet = user.getWallet();
      if (wallet != null && user.getWallet().getBalance().compareTo(BigDecimal.ZERO) > 0){
          throw new WalletHasBalanceException("Ops ocorreu um erro, O Saldo tem que ser igual a zero para deletar a conta");
      }

      if (transactionRepository.findFirstBySender_User_IdAndStatus(id,TransactionStatus.PENDENTE)
              .isPresent()){
          throw new TransactionPendingException("Ops erro, você tem uma transação pendente, e não pode deletar a conta");
      }
        VirtualCard virtualCard = user.getVirtualCard();
      if (virtualCard != null && user.getVirtualCard().getCardStatus().equals(VirtualCardStatus.ATIVO)){
          if ( user.getVirtualCard().getCardLimit().compareTo(BigDecimal.ZERO) > 0){
              throw new VirtualCardLimitException("Ops vc so pode deletar se o cartão estiver bloqueado e sem uso pendente");
          }
      }

      walletService.blockWallet(user.getWallet().getId());
      virtualCardService.blockVirtualCard(user.getVirtualCard().getId());

      refreshTokenRepository.findByUser(user)
              .ifPresent(refreshTokenRepository::delete);

      userRepository.delete(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado"));
    }

    public void updatePassword(Long userId, String newPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}