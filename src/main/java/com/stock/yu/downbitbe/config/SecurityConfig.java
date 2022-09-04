package com.stock.yu.downbitbe.config;

import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.domain.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.security.filter.JwtAuthenticationFilter;
import com.stock.yu.downbitbe.security.filter.JwtAuthorizationFilter;
import com.stock.yu.downbitbe.security.handler.ApiLoginFailHandler;
import com.stock.yu.downbitbe.security.handler.UserLoginSuccessHandler;
import com.stock.yu.downbitbe.security.filter.ApiCheckFilter;
import com.stock.yu.downbitbe.security.filter.ApiLoginFilter;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@Log4j2
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //권한 관리의 다른 방법
@EnableGlobalMethodSecurity(prePostEnabled = true) //권한 관리의 다른 방법
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomUserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception {
        //AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();


        //반드시 필요
        http.authenticationManager(authenticationManager);

        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //.and()
//                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil()), UsernamePasswordAuthenticationFilter.class)
                //.addFilter(new JwtAuthorizationFilter(authenticationManager, , jwtUtil()));
        //           .addFilterBefore(new JwtAuthorizationFilter(authenticationManager, userRepository), UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new JwtAuthorizationFilter(authenticationManager, customUserDetailsService, jwtUtil()), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/sample/all", "/login", "/logout").permitAll()
                .antMatchers("/sample/member").hasRole("USER") // USER 는 스프링 내부에서 인증된 사용자를 의미함
                .antMatchers("/api/v2/token/**").hasRole("USER")
                .antMatchers("/api/v2/signup", "/api/v2/login", "/api/v1/login", "/images/**", "/api/v2/users/**").permitAll();
        http.formLogin().loginPage(Config.WEB_BASE_URL+"/login").usernameParameter("username").passwordParameter("password").loginPage("/api/v2/login");
        http.cors().and().csrf().disable();


        http.oauth2Login().successHandler(successHandler())
                .defaultSuccessUrl(Config.WEB_BASE_URL);
        //http.rememberMe().tokenValiditySeconds(60*60*24*7).userDetailsService(userDetailsService); // auto login during 7days

        http.logout()
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .addLogoutHandler((request, response, authentication) -> {
                    Cookie token = new Cookie("token", null);
                    token.setPath("/");
                    token.setHttpOnly(true);
                    token.setMaxAge(-1);
                    token.setDomain(Config.DOMAIN);

                    response.addCookie(token);

                    SecurityContextHolder.clearContext();
                })
                .deleteCookies("token", Config.WEB_BASE_URL)
                .invalidateHttpSession(true);

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
    public UserLoginSuccessHandler successHandler() {
        return new UserLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.setAllowedOrigins(Collections.singletonList("http://42.82.185.184:3000")); // singletonList : 하나짜리 리스트
        configuration.setAllowedOrigins(Arrays.asList(Config.WEB_BASE_URL, "http://localhost:8080"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

    @Bean
    AccessDecisionVoter hierarchyVoter() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return new RoleHierarchyVoter(hierarchy);
    }


}
