package com.algo.yu.algobe.user.domain.userBlock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    Page<UserBlock> findAllByUserId_UserId(Pageable pageable, Long userId);
    Boolean existsByBlockUserId_UserIdAndUserId_UserId(Long blockUserId, Long userBlockId);

}
