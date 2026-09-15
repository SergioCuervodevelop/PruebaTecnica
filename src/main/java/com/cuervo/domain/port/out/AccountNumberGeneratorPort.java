package com.cuervo.domain.port.out;

import com.cuervo.domain.enums.AccountType;

public interface AccountNumberGeneratorPort {

    String generate(AccountType accountType);
}