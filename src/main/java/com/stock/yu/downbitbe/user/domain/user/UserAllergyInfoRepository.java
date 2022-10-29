package com.stock.yu.downbitbe.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAllergyInfoRepository extends JpaRepository<UserAllergyInfo, Long> {
    UserAllergyInfo findByUserId(Long userIId);
}
