package com.stock.yu.downbitbe.security;

import com.stock.yu.downbitbe.user.entity.Grade;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
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
                    .userId("user"+i+"@zerock.org")
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
        Optional<User> result = repository.findByUserIdAndType("user95@zerock.org", LoginType.LOCAL);
        User user = result.get();
        System.out.println(user);
    }
}
