package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.user.application.UserAllergyInfoService;
import com.stock.yu.downbitbe.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final UserAllergyInfoService userAllergyInfoService;
    private final UserService userService;

    @GetMapping("/{nickname}/allergyinfo")
    public ResponseEntity<Map<String, Boolean>> getAllergyInfo(@RequestBody @PathVariable("nickname") String nickname) {
        Map<String, Boolean> allergyMap = userAllergyInfoService.findUserAllergyInfoByUser(userService.findByNickname(nickname));

        return ResponseEntity.ok(allergyMap);
    }
}
