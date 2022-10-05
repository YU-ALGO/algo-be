package com.stock.yu.downbitbe.user.service;

import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsService loadUserByUsername " + username);

        //TODO : 확인 필요
        //Optional<User> result = customUserRepository.findByUserIdAndType(username, LoginType.LOCAL);
        User user = customUserRepository.findByUserId(username);

//        if(result.isEmpty()) {
//            throw new UsernameNotFoundException("Check Email or Social ");
//        }
//
//        User user = result.get();
        //////////////////////////////////

        /*List<User> result = customUserRepository.findByUserIdAndType(username, LoginType.LOCAL);
        if(result.isEmpty()) {
            throw new UsernameNotFoundException(("Check Email or Social "));
        }
        User user = result.get(0);*/

        log.info("---------------------------");
        log.info(user);

        UserAuthDTO userAuth = new UserAuthDTO(
                user.getUserId(),
                user.getPassword(),
                user.getType(),
                user.getGradeSet().stream().map( grade ->
                        new SimpleGrantedAuthority("ROLE_"+grade.name())
                ).collect(Collectors.toSet())
        );

        userAuth.setNickname(user.getNickname());
        userAuth.setType(user.getType());

        return userAuth;
    }


}