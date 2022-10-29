package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.domain.user.UserAllergyInfo;
import com.stock.yu.downbitbe.user.domain.user.UserAllergyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserAllergyInfoService {
    private final UserAllergyInfoRepository userAllergyInfoRepository;

    @Transactional
    public Map<String, Boolean> findUserAllergyInfoByUser(User user) {
        UserAllergyInfo userAllergyInfo = userAllergyInfoRepository.findByUserId(user.getUserId());
        return userAllergyInfo.toMap();
    }
}
