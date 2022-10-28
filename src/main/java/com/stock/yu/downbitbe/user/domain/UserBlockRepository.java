package com.stock.yu.downbitbe.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Page<UserBlock> findAllByUserId(Pageable pageable, Long userId);
}
