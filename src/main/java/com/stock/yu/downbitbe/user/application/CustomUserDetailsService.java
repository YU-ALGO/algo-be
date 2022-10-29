package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.domain.user.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserRepository customUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsService loadUserByUsername " + username);

        //TODO : Ȯ�� �ʿ�
        //Optional<User> result = customUserRepository.findByUserIdAndType(username, LoginType.LOCAL);
        User user = customUserRepository.findByUsername(username);

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

        UserAuthDto userAuth = new UserAuthDto(
                user.getUsername(),
                user.getPassword(),
                user.getLoginType(),
                user.getGradeSet().stream().map( grade ->
                        new SimpleGrantedAuthority("ROLE_"+grade.name())
                ).collect(Collectors.toSet())
        );

        userAuth.setNickname(user.getNickname());
        userAuth.setLoginType(user.getLoginType());

        return userAuth;
    }


}