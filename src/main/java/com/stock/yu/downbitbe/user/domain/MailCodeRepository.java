package com.stock.yu.downbitbe.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailCodeRepository extends JpaRepository<MailCode, Long> {

    Optional<MailCode> findByUser(User user);



}