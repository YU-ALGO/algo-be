package com.stock.yu.downbitbe.user.repository;

import com.stock.yu.downbitbe.user.entity.MailCode;
import com.stock.yu.downbitbe.user.entity.UserAllergyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAllergyInfoRepository extends JpaRepository<UserAllergyInfo, Long> {
    UserAllergyInfo findByUserId(Long userIId);
}
