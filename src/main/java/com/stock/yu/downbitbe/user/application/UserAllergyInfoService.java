package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.user.domain.user.*;
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
    private final CustomUserRepository userRepository;

    @Transactional
    public Map<String, Boolean> findUserAllergyInfoByUser(UserAuthDto userAuth) {
        User user = userRepository.findByUsername(userAuth.getUsername());

        AllergyInfoDto userAllergyInfo = userAllergyInfoRepository.findAllergyDtoByUserId(user.getUserId());
        return userAllergyInfo.toMap();
    }

    @Transactional(readOnly = true)
    public Boolean existsUserByUserId(String username) {
        return userAllergyInfoRepository.existsByUserUsername(username);
    }

    @Transactional
    public UserAllergyInfo saveAllergyInfo(String username) {
        User user = userRepository.findByUsername(username);
        UserAllergyInfo userAllergyInfo =
                UserAllergyInfo.builder()
                        .user(user).build();
        return userAllergyInfoRepository.save(userAllergyInfo.updateAllergyInfo(new AllergyInfo()));
    }
}
