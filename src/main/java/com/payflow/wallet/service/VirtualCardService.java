package com.payflow.wallet.service;


import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VirtualCardService {

    public String generateRandomCardNumber() {
        Random random = new Random();

        return random.ints(16, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

}
