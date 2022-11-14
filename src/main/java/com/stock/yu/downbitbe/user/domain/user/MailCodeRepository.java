package com.stock.yu.downbitbe.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailCodeRepository extends JpaRepository<MailCode, Long>, MailCodeRepositoryCustom {

    Optional<MailCode> findByUsername(String username);

}