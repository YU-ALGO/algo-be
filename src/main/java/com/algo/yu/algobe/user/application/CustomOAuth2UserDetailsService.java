package com.algo.yu.algobe.user.application;

import com.algo.yu.algobe.user.domain.user.*;
import com.algo.yu.algobe.user.utils.RandomCodeUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        String profileImgUrl = null;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        //TODO : profile 사진 추가하기
        switch (clientName) {
            case "Google":
                email = (String) attributes.get("email");
                type = LoginType.GOOGLE;
                profileImgUrl = (String) attributes.get("picture");
                break;
            case "Naver": {
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
                attributes = response;
                email = (String) response.get("email");
                nickname = (String) response.get("nickname");
                type = LoginType.NAVER;
                profileImgUrl = (String) response.get("profile_image");
                break;
            }
            case "Kakao": {
                Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
                attributes = response;
                email = (String) response.get("email");
                nickname = (String) ((Map<String, Object>) response.get("profile")).get("nickname");
                type = LoginType.KAKAO;
                profileImgUrl = (String) response.get("profile_image");
                break;
            }
        }
        log.info("EMAIL: " + email);

        while (existsByNickname(nickname))
            nickname = clientName + RandomCodeUtil.createCode();


        User user = saveSocialMember(email+clientName, nickname, type, profileImgUrl);


        //User newUser = repository.findByUsername(user.getUsername());
        //log.info("newUser : " + newUser);
        //saveUserAllergyInfo(newUser);


        UserAuthDto userAuth = new UserAuthDto(
                user.getUsername(),
                user.getPassword(),
                user.getLoginType(),
                user.getGradeSet().stream().map( grade -> new SimpleGrantedAuthority("ROLE_"+grade.name()))
                        .collect(Collectors.toList()),
                attributes
        );
        userAuth.setNickname(user.getNickname());

        return userAuth;
    }

    // for google login
    @Transactional
    public User saveSocialMember(String email, LoginType type, String profileImage) {

        Optional<User> result = repository.findByUsernameAndLoginType(email, type);

        if (result.isPresent()) {
            return result.get();
        }

        String nickname = "google" + RandomCodeUtil.createCode();
        while (existsByNickname(nickname))
            nickname = "google" + RandomCodeUtil.createCode();

        User user = User.builder()
                .username(email)
                .nickname(nickname)    //TODO 임시 닉네임 : email
                .password( passwordEncoder.encode("1111")) //TODO 임시 비밀번호 : 1111
                .loginType(type)
                .profileImg(profileImage)
                .build();

        user.addGrade(Grade.USER);
        repository.saveAndFlush(user);

        return user;
    }

    // for kakao, naver login
    @Transactional
    public User saveSocialMember(String email, String nickname, LoginType type, String profileImage) {

        if (type.equals(LoginType.GOOGLE))
            return saveSocialMember(email, type, profileImage);

        Optional<User> result = repository.findByUsernameAndLoginType(email, type);

        if (result.isPresent()  && result.get().getLoginType() != LoginType.LOCAL) {
            return result.get();
        }

        User user = User.builder()
                .username(email)
                .nickname(nickname)
                .password( passwordEncoder.encode("1111")) //TODO 임시 비밀번호 : 1111
                .loginType(type)
                .profileImg(profileImage)
                .build();

        user.addGrade(Grade.USER);
        repository.saveAndFlush(user);

        return user;
    }

    public User saveUser(User user) {
        validateDuplicateUser(user);
        return repository.save(user);
    }

    private void validateDuplicateUser(User user) {
        User findUser = repository.findByUsername(user.getUsername());
        if(user != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        if (findUser.getLoginType() == LoginType.LOCAL)
            throw new IllegalStateException("회원 시스템에 등록된 계정입니다");
    }

    @Transactional(readOnly = true)
    public Boolean existsByNickname(String nickname) {
        return repository.existsByNickname(nickname);
    }

}