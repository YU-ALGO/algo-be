package com.stock.yu.downbitbe.user.service;

import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.entity.UserAllergyInfo;
import com.stock.yu.downbitbe.user.repository.UserAllergyInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

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
