package com.stock.yu.downbitbe.user.repository;

import com.stock.yu.downbitbe.user.entity.MailCode;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MailCodeRepository extends JpaRepository<MailCode, Long> {

    Optional<MailCode> findByUser(User user);



}