package com.stock.yu.downbitbe.message.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Page<UserBlockListDto> findAllByUserId(Pageable pageable, Long userId);
}
