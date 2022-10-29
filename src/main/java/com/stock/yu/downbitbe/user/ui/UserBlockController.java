package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.user.application.UserBlockService;
import com.stock.yu.downbitbe.user.domain.userBlock.UserBlock;
import com.stock.yu.downbitbe.user.domain.userBlock.UserBlockListDto;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDTO;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.application.UserService;
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
@RequestMapping("/api/v1/users/blocks")
public class UserBlockController {
    private final UserBlockService userBlockService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserBlockListDto>> getUserBlockList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<UserBlock> userBlockListDto = userBlockService.findAllBlocksByUserId(pageable, user.getUserId());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(userBlockListDto.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(userBlockListDto.stream().map(UserBlockListDto::new).collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<Long> createUserBlock(@RequestBody Map<String, String> UserBlockCreateRequestDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        User userBlock = userService.findByUsername(UserBlockCreateRequestDto.get("blockUserName"));
        Long ret = userBlockService.createUserBlock(user, userBlock);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("{block_id}")
    public ResponseEntity<Long> deleteUserBlock(@PathVariable("block_id") Long blockId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = userBlockService.deleteUserBlock(user, blockId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}
