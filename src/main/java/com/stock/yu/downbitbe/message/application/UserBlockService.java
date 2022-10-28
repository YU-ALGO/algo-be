package com.stock.yu.downbitbe.message.application;

import com.stock.yu.downbitbe.message.domain.UserBlock;
import com.stock.yu.downbitbe.message.domain.UserBlockListDto;
import com.stock.yu.downbitbe.message.domain.UserBlockRepository;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserBlockService {
    private final UserBlockRepository userBlockRepository;

    @Transactional(readOnly = true)
    public Page<UserBlockListDto> findAllBlocksByUserId(Pageable pageable, Long userId){
        return userBlockRepository.findAllByUserId(pageable, userId);
    }

    public Long createUserBlock(User user, User blockUser) {
        UserBlock messageBlock = UserBlock.builder().user(user).blockUserId(blockUser).build();
        return userBlockRepository.save(messageBlock).getUserBlockId();
    }
}
