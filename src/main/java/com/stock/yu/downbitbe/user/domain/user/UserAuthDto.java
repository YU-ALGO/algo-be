package com.stock.yu.downbitbe.user.domain.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class UserAuthDto extends User implements OAuth2User {

    private String username;
    private String nickname;
    private LoginType loginType;
    private Map<String, Object> attr; // OAuth2User 는 Map 타입으로 모든 인증 결과를 attributes 라는 이름으로 가짐

    private String name; // OAuth2User 를 상속받기 위해 필수적으로 필요함 -> 현재 구조상 사용되지않음(null)

    public UserAuthDto(String username, String password, LoginType type, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username, password, type, authorities);
        this.attr = attr;
    }

    public UserAuthDto(String username, String password, LoginType loginType, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.loginType = loginType;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

}