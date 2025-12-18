package com.payflow.wallet.service;


import com.payflow.wallet.dto.virtualcard.VirtualCardStatusResponse;
import com.payflow.wallet.entity.VirtualCard;
import com.payflow.wallet.enums.cardvirtualenum.VirtualCardStatus;
import com.payflow.wallet.exception.VirtualCardInactiveException;
import com.payflow.wallet.exception.VirtualCardNotFound;
import com.payflow.wallet.repository.VirtualCardRepository;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VirtualCardService {
    private final VirtualCardRepository virtualCardRepository;
    private final SecurityValidator securityValidator;

    public VirtualCardService(VirtualCardRepository virtualCardRepository, SecurityValidator securityValidator) {
        this.virtualCardRepository = virtualCardRepository;
        this.securityValidator = securityValidator;
    }

    public String generateRandomCardNumber() {
        Random random = new Random();

        return random.ints(16, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
    public VirtualCardStatusResponse blockVirtualCard(Long virtualCardId) {
        VirtualCard virtualCard = virtualCardRepository.findById(virtualCardId)
                .orElseThrow(() -> new VirtualCardNotFound("Essa Cartão não foi encontrado"));

        securityValidator.validateSameUser(virtualCard.getUser().getId());

        if (virtualCard.getCardStatus().equals(VirtualCardStatus.BLOQUEADO)) {
            throw new VirtualCardInactiveException("O cartão ja esta bloqueado");
        }
        virtualCard.setCardStatus(VirtualCardStatus.BLOQUEADO);

        virtualCardRepository.save(virtualCard);

        return new VirtualCardStatusResponse(
                virtualCard.getCardStatus()
        );
    }
    public VirtualCardStatusResponse unblockVirtualCard(Long virtualCardId){
        VirtualCard virtualCard = virtualCardRepository.findById(virtualCardId)
                .orElseThrow(() -> new VirtualCardNotFound("Essa Cartão não foi encontrado"));


        securityValidator.validateSameUser(virtualCard.getUser().getId());

        if (virtualCard.getCardStatus() != VirtualCardStatus.BLOQUEADO) {
            throw new VirtualCardInactiveException("Só é possível desbloquear cartões bloqueados.");
        }
        virtualCard.setCardStatus(VirtualCardStatus.ATIVO);

        virtualCardRepository.save(virtualCard);

        return new VirtualCardStatusResponse(
                virtualCard.getCardStatus()
        );
    }
}
