package com.algo.yu.algobe.user.ui;

import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import com.algo.yu.algobe.user.domain.userBlock.UserBlock;
import com.algo.yu.algobe.user.domain.userBlock.UserBlockListDto;
import com.algo.yu.algobe.user.application.UserBlockService;
import com.algo.yu.algobe.user.application.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class UserBlockController {
    private final UserBlockService userBlockService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserBlockListDto>> getUserBlockList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<UserBlock> userBlockListDto = userBlockService.findAllBlocksByUserId(pageable, user.getUserId());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(userBlockListDto.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(userBlockListDto.stream().map(UserBlockListDto::new).collect(Collectors.toList()));
    }

    @PostMapping("")
    public ResponseEntity<Long> createUserBlock(@RequestBody Map<String, String> UserBlockCreateRequestDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        String blockNickname = UserBlockCreateRequestDto.get("blockUserName");
        if(user.getNickname().equals(blockNickname))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        User userBlock = userService.findByNickname(blockNickname);
        if(userBlockService.existsByBlockUserIdAndUserBlockId(user, userBlock))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Long ret = userBlockService.createUserBlock(user, userBlock);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("{block_id}")
    public ResponseEntity<Long> deleteUserBlock(@PathVariable("block_id") Long blockId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = userBlockService.deleteUserBlock(user, blockId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}
