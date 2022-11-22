package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.user.domain.userBlock.UserBlock;
import com.stock.yu.downbitbe.user.domain.userBlock.UserBlockRepository;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserBlockService {
    private final UserBlockRepository userBlockRepository;

    @Transactional(readOnly = true)
    public Page<UserBlock> findAllBlocksByUserId(Pageable pageable, Long userId){
        return userBlockRepository.findAllByUserId_UserId(pageable, userId);
    }

    public Long createUserBlock(User user, User blockUser) {
        UserBlock userBlock = UserBlock.builder().user(user).blockUserId(blockUser).build();
        return userBlockRepository.save(userBlock).getUserBlockId();
    }

    public Long deleteUserBlock(User user, Long blockId) {
        UserBlock userBlock = userBlockRepository.findById(blockId).orElseThrow(() -> new IllegalArgumentException("차단 내용이 없습니다."));
        if(!Objects.equals(userBlock.getUserId().getUserId(), user.getUserId()))
            throw new RuntimeException("사용자가 일치하지 않습니다.");
        userBlockRepository.delete(userBlock);
        return userBlock.getUserBlockId();
    }

    public Boolean existsByBlockUserIdAndUserBlockId(User blockUser, User userBlock) {
        return userBlockRepository.existsByBlockUserIdAndUserBlockId(blockUser.getUserId(), userBlock.getUserId());
    }
}
