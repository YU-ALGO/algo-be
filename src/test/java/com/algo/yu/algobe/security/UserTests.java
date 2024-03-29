package com.algo.yu.algobe.security;

import com.algo.yu.algobe.user.domain.user.Grade;
import com.algo.yu.algobe.user.domain.user.LoginType;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.CustomUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class UserTests {

    @Autowired
    private CustomUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    @DisplayName("회원 생성 테스트(100명)")
    public void insertDummies() {
        //1 - 80 : USER
        //81 - 100 : ADMIN

        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .username("user"+i+"@test")
                    .nickname("사용자"+i)
                    .loginType(LoginType.LOCAL)
                    .password( passwordEncoder.encode("1111"))
                    .build();

            user.addGrade(Grade.USER);
            if(i>80)
                user.addGrade(Grade.ADMIN);
            repository.save(user);
        });
    }

    @Test
    @Transactional
    @DisplayName("admin 생성")
    public void insertAdmin() {
        User user = User.builder()
                .username("admin")
                .nickname("admin")
                .loginType(LoginType.LOCAL)
                .password(passwordEncoder.encode("admin"))
                .build();

        user.addGrade(Grade.ADMIN);

        repository.save(user);
    }

    @Test
    public void testRead() {
        Optional<User> result = repository.findByUsernameAndLoginType("user95@test", LoginType.LOCAL);
        User user = result.get();
        System.out.println(user);
    }

    @Test
    @Transactional
    @DisplayName("회원가입 테스트")
    public void signupTest() {

    }
}
