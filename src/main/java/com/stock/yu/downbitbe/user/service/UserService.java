package com.stock.yu.downbitbe.user.service;

import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    final private CustomUserRepository userRepository;

    public Optional<User> findByUsernameAndLoginType(String username, LoginType type) {
        return userRepository.findByUsernameAndLoginType(username, type);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean existsByUsername(String username) {
        return existsByUsername(username);
    }

    public Boolean existsByNickname(String nickname) {
        return existsByNickname(nickname);
    }
}
