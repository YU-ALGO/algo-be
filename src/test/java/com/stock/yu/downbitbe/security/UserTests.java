package com.stock.yu.downbitbe.security;

import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.domain.user.entity.Grade;
import com.stock.yu.downbitbe.domain.user.entity.LoginType;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public void insertDummies() {
        //1 - 80 : USER
        //81 - 100 : ADMIN

        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .userId("user"+i+"@test")
                    .nickname("사용자"+i)
                    .type(LoginType.LOCAL)
                    .password( passwordEncoder.encode("1111"))
                    .build();

            user.addGrade(Grade.USER);
            if(i>80)
                user.addGrade(Grade.ADMIN);
            repository.save(user);
        });
    }

    @Test
    public void testRead() {
        Optional<User> result = repository.findByUserIdAndType("user95@test", LoginType.LOCAL);
        User user = result.get();
        System.out.println(user);
    }

    @Test
    @Transactional
    @DisplayName("회원가입 테스트")
    public void signupTest() {


    }
}