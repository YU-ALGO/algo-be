package com.stock.yu.downbitbe.domain.user.service;

import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.domain.user.entity.Grade;
import com.stock.yu.downbitbe.domain.user.entity.LoginType;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final CustomUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("----------------");
        log.info("userRequest: " + userRequest); // org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest 객체

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("=====================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k+": "+v);
        });

        String email = null;
        String nickname = null;
        LoginType type = null;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        //TODO : profile 사진 추가하기
        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            type = LoginType.GOOGLE;
        } else if (clientName.equals("Naver")) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            attributes = response;
            email = (String) response.get("email");
            nickname = (String) response.get("nickname");
            type = LoginType.NAVER;
        } else if (clientName.equals("Kakao")) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            attributes = response;
            email = (String) response.get("email");
            nickname = (String)((Map<String, Object>) response.get("profile")).get("nickname");
            type = LoginType.KAKAO;
        }
        log.info("EMAIL: " + email);
        User user = saveSocialMember(email, nickname, type);

        //return oAuth2User;

        UserAuthDTO userAuth = new UserAuthDTO(
                user.getUserId(),
                user.getPassword(),
                user.getType(),
                user.getGradeSet().stream().map( grade -> new SimpleGrantedAuthority("ROLE_"+grade.name()))
                        .collect(Collectors.toList()),
                attributes
        );
        userAuth.setNickname(user.getNickname());

        return userAuth;
    }

    private User saveSocialMember(String email, LoginType type) {

        Optional<User> result = repository.findByUserIdAndType(email, type);

        if(result.isPresent()) {
            return result.get();
        }

        User user = User.builder()
                .userId(email)
                .nickname(email)    //TODO 임시 닉네임 : email
                .password( passwordEncoder.encode("1111")) //TODO 임시 비밀번호 : 1111
                .type(type)
                .build();

        user.addGrade(Grade.USER);
        repository.save(user);

        return user;
    }

    private User saveSocialMember(String email, String nickname, LoginType type) {

        if(type.equals(LoginType.GOOGLE))
            return saveSocialMember(email, type);

        Optional<User> result = repository.findByUserIdAndType(email, type);

        if(result.isPresent()) {
            return result.get();
        }

        User user = User.builder()
                .userId(email)
                .nickname(nickname)
                .password( passwordEncoder.encode("1111")) //TODO 임시 비밀번호 : 1111
                .type(type)
                .build();

        user.addGrade(Grade.USER);
        repository.save(user);

        return user;
    }


}
