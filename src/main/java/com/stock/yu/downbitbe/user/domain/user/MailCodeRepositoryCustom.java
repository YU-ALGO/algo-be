package com.stock.yu.downbitbe.user.domain.user;

public interface MailCodeRepositoryCustom {
    Boolean findIsValidateByUsername(String username);
}
