package com.payflow.wallet.service;

import com.payflow.wallet.dto.transaction.TransactionRequest;
import com.payflow.wallet.dto.user.UserRequest;
import com.payflow.wallet.dto.wallet.BalanceResponse;
import com.payflow.wallet.dto.wallet.WalletRequest;
import com.payflow.wallet.dto.wallet.WalletResponse;
import com.payflow.wallet.dto.wallet.WalletStatusResponse;
import com.payflow.wallet.entity.Transaction;
import com.payflow.wallet.entity.User;
import com.payflow.wallet.entity.Wallet;
import com.payflow.wallet.enums.transactionsenums.TransactionStatus;
import com.payflow.wallet.enums.transactionsenums.TransactionType;
import com.payflow.wallet.enums.walletenums.WalletStatus;
import com.payflow.wallet.exception.*;
import com.payflow.wallet.repository.TransactionRepository;
import com.payflow.wallet.repository.UserRepository;
import com.payflow.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final SecurityValidator securityValidator;
    private final TransactionRepository transactionRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, SecurityValidator securityValidator, TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.securityValidator = securityValidator;
        this.transactionRepository = transactionRepository;
    }
    public Wallet createWallet(User user) {
        walletRepository.findByUserId(user.getId())
                .ifPresent(wallet -> {
                    throw new UserAlreadyHasWalletException("Usuário já possui uma carteira.");
                });
        Wallet wallet = Wallet.builder()
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ATIVA)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        return walletRepository.save(wallet);
    }
    public WalletResponse getWalletByUserId(Long userId){

        securityValidator.validateUserOrAdmin(userId);

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada para o usuário " + userId));

        return new  WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getUser().getId(),
                wallet.getCreatedAt()
        );

    }

    public WalletResponse deposit(Long walletId, TransactionRequest transactionRequest){
       Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Carteira não encontrada"));

       securityValidator.validateSameUser(wallet.getUser().getId());

       if(wallet.getStatus() != WalletStatus.ATIVA){
           throw new WalletInactiveException("Essa carteira não está ativa");
       }

       validatePositiveAmount(transactionRequest.amount());

       var saldoAtual = wallet.getBalance();
        var novoSaldo = saldoAtual.add(transactionRequest.amount());
        wallet.setBalance(novoSaldo);

        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.amount())
                .type(TransactionType.DEPOSITO)
                .status(TransactionStatus.CONCLUIDO)
                .sender(wallet)
                .receiver(wallet)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);

        return new WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getUser().getId(),
                wallet.getCreatedAt()
        );



    }

    private void validatePositiveAmount(BigDecimal amount){
        if (amount == null || (amount.compareTo(BigDecimal.ZERO) <= 0)){
            throw new InvalidAmountException("O valor deve ser maior que zero.");
        }
    }
    private void validateSufficientBalance(BigDecimal balance, BigDecimal amount){
        if(balance.compareTo(amount) < 0){
            throw new InvalidAmountException("Saldo Insuficiente");
        }
    }
    private void validateLimitDay(BigDecimal limitDay, Long userId, BigDecimal valueTransaction) {
        LocalDateTime dataDoDia = LocalDateTime.now();
        LocalDateTime inicioDoDia = dataDoDia.toLocalDate().atStartOfDay();
        LocalDateTime fimDoDia = dataDoDia.toLocalDate().atTime(23, 59, 59);


        List<Transaction> trasacoesHoje = transactionRepository.findBySender_User_IdAndCreatedAtBetween(userId, inicioDoDia, fimDoDia);

        BigDecimal totalHoje = trasacoesHoje.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalComAtual = totalHoje.add(valueTransaction);

        if (totalComAtual.compareTo(limitDay) > 0) {
            throw new LimitTransactionException("Limite diário excedido. Limite: " + limitDay);
        }
    }




    public WalletResponse withdraw(Long walletId, TransactionRequest transactionRequest, WalletRequest walletRequest){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Essa Carteira não foi encontrada"));

        securityValidator.validateSameUser(wallet.getUser().getId());

        if (wallet.getStatus() != WalletStatus.ATIVA){
            throw new WalletInactiveException("Essa carteira não está ativa");
        }
        validatePositiveAmount(transactionRequest.amount());
        validateSufficientBalance(wallet.getBalance(), transactionRequest.amount());
        validateLimitDay(BigDecimal.valueOf(1000),wallet.getUser().getId(),transactionRequest.amount());

        var saldoAtual = wallet.getBalance();
        var novoSaldo =  saldoAtual.subtract(transactionRequest.amount());
        wallet.setBalance(novoSaldo);

        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.amount())
                .type(TransactionType.SAQUE)
                .status(TransactionStatus.CONCLUIDO)
                .sender(wallet)
                .receiver(wallet)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);


        return new WalletResponse(
                wallet.getId(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getUser().getId(),
                wallet.getCreatedAt()
        );


    }

    public WalletStatusResponse blockWallet(Long walletId){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Essa Carteira não foi encontrada"));

        securityValidator.validateSameUser(wallet.getUser().getId());

        if(wallet.getStatus() == WalletStatus.BLOQUEADA){
            throw new WalletInactiveException("A carteira ja esta bloqueada");
        }
        wallet.setStatus(WalletStatus.BLOQUEADA);

        walletRepository.save(wallet);

        return new WalletStatusResponse(
                wallet.getStatus()
        );
    }

    public WalletStatusResponse unblockWallet(Long walletId){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Essa Carteira não foi encontrada"));

        securityValidator.validateSameUser(wallet.getUser().getId());

        if(wallet.getStatus() != WalletStatus.BLOQUEADA){
            throw new WalletInactiveException("Só é possível desbloquear carteiras bloqueadas.");
        }
        wallet.setStatus(WalletStatus.ATIVA);

        walletRepository.save(wallet);

        return new WalletStatusResponse(
                wallet.getStatus()
        );
    }

    public BalanceResponse getBalance(Long walletId){
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Essa Carteira não foi encontrada"));

        securityValidator.validateSameUser(wallet.getUser().getId());

        return new BalanceResponse(
                wallet.getBalance()
        );
    }
    }

