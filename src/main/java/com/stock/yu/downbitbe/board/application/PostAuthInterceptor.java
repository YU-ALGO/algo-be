package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
//public class PostAuthInterceptor implements HandlerInterceptor {
//    @Autowired
//    CustomUserRepository userRepository;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }
//}
