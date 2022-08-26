package com.stock.yu.downbitbe.controller;

import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/all")
    public void exAll() {
        log.info("exAll.....");
    }

/*    @GetMapping("/member")
    public void exMember() {
        log.info("exMember...");
    }*/

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal UserAuthDTO userAuth) {
        log.info("exMember...");
        log.info("--------------------");
        log.info(userAuth);
    }

    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin...");
    }

    @PreAuthorize("#userAuth != null && #userAuth.userId eq \"user95@zerock.org\"") //특정 유저에게만 권한주기
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal UserAuthDTO userAuth) {
        log.info("exMemberOnly.................");
        log.info(userAuth);

        return "/sample/admin";
    }
}
