package com.stock.yu.downbitbe.message.ui;

import com.stock.yu.downbitbe.message.application.UserBlockService;
import com.stock.yu.downbitbe.message.domain.UserBlockListDto;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages/blocks")
public class UserBlockController {
    private final UserBlockService userBlockService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserBlockListDto>> getBlockUserList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<UserBlockListDto> blockUserList = userBlockService.findAllBlocksByUserId(pageable, user.getUserId());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(blockUserList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(blockUserList.stream().collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<Long> createBlockUser(@RequestBody Map<String, String> blockUserCreateDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        User blockUser = userService.findByUsername(blockUserCreateDto.get("blockUserName"));
        Long ret = userBlockService.createUserBlock(user, blockUser);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

//    @DeleteMapping("{block_id}")
//    public ResponseEntity<Long> deleteBlockUser(@PathVariable("block_id") Long blockId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
//        User user = userService.findByUsername(auth.getUsername());
//
//        return ResponseEntity.status(HttpStatus.OK).body(ret);
//    }
}
