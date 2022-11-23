package com.algo.yu.algobe.user.domain.user;

public interface MailCodeRepositoryCustom {
    Boolean findIsValidateByUsername(String username);
}
