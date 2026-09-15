package com.cuervo.infrastructure.persistence.adapter;

import com.cuervo.domain.enums.AccountType;
import com.cuervo.domain.port.out.AccountNumberGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGeneratorAdapter
        implements AccountNumberGeneratorPort {

    @Override
    public String generate(AccountType accountType) {

        String prefix = accountType == AccountType.SAVINGS
                ? "53"
                : "33";

        int remainingDigits = 8;

        int min = 10000000;
        int max = 99999999;

        int randomNumber =
                ThreadLocalRandom.current().nextInt(min, max + 1);

        return prefix + randomNumber;
    }
}