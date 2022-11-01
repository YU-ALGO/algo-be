package com.stock.yu.downbitbe.user.application;

import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.user.domain.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final CustomUserRepository userRepository;
    private final UserAllergyInfoRepository userAllergyInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findByUsernameAndLoginType(String username, LoginType type) {
        return userRepository.findByUsernameAndLoginType(username, type);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    @Transactional
    public void signUp(SignupRequestDto request) {
        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .loginType(LoginType.LOCAL)
                .build();

        newUser.addGrade(Grade.USER);



        userRepository.save(newUser);
    }

    @Transactional
    public void passwordChange(UserAuthDto userAuthDTO, String password) {
        User user = userRepository.findByUsername(userAuthDTO.getUsername());
        String newEncodingPassword = passwordEncoder.encode(password);

        /* 기존 비밀번호와 일치 여부 확인 */
        if(user.getPassword().equals(newEncodingPassword))
            throw new RuntimeException("이전 비밀번호와 일치합니다.");

        user.updatePassword(newEncodingPassword);
        userRepository.save(user);

    }

    @Transactional
    public void nicknameChange(UserAuthDto userAuthDTO, String newNickname) {
        User user = userRepository.findByUsername(userAuthDTO.getUsername());

        user.updateNickname(newNickname);
        userRepository.save(user);

    }

    @Transactional
    public void introduceChange(UserAuthDto userAuthDTO, String newIntroduce) {
        User user = userRepository.findByUsername(userAuthDTO.getUsername());

        user.updateIntroduce(newIntroduce);
        userRepository.save(user);

    }

    @Transactional
    public void allergyChange(UserAuthDto userAuthDTO, AllergyInfo newAllergyInfo) {
        User user = userRepository.findByUsername(userAuthDTO.getUsername());

        UserAllergyInfo userAllergyInfo = userAllergyInfoRepository.findByUserId(user.getUserId());
        userAllergyInfo.updateAllergyInfo(newAllergyInfo);

        userAllergyInfoRepository.save(userAllergyInfo);
    }

    @Transactional
    public void leave(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(passwordEncoder.encode(password).equals(user.getPassword()))
            userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
