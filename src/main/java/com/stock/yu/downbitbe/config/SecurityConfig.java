package com.stock.yu.downbitbe.config;

import com.stock.yu.downbitbe.user.handler.UserLoginSuccessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@Log4j2
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //권한 관리의 다른 방법
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();

        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/api/v1/**", "api/v1/boards/1/posts").permitAll()
                .antMatchers("/sample/member").hasRole("USER"); // USER 는 스프링 내부에서 인증된 사용자를 의미함
        http.formLogin();


        http.oauth2Login().successHandler(successHandler())
                .defaultSuccessUrl("/sample/member");
        http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService); // auto login during 7days

        http.logout();
        /*
        커스텀 로그인 화면
        loginPage()
        loginProcessUrl()
        defaultSuccessUrl()
        failureUrl()
        로그아웃 후 연결될 링크
        logoutSuccessUrl()
        logoutUrl()
        쿠키 무효화
        deleteCookies()
        invalidatedHttpSession()
         */


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://42.82.185.184:3000")); // singletonList : 하나짜리 리스트
        //configuration.setAllowedOrigins(Arrays.asList(Config.WEB_BASE_URL, "http://localhost:3000"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserLoginSuccessHandler successHandler() {
        return new UserLoginSuccessHandler(passwordEncoder());
    }

}
