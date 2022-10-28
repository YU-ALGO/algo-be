package com.stock.yu.downbitbe.message.ui;

import com.stock.yu.downbitbe.message.application.MessageService;
import com.stock.yu.downbitbe.message.domain.ReceiveMessageListDto;
import com.stock.yu.downbitbe.message.domain.SendMessageListDto;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/inboxes")
    public ResponseEntity<List<ReceiveMessageListDto>> getReceiveMessageList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                             @RequestParam(value = "not_read", required = false) Boolean notRead,
                                                                             @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<ReceiveMessageListDto> receiveMessageList = messageService.findAllMessagesByReceiver(user.getUserId(), notRead, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(receiveMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(receiveMessageList.stream().collect(Collectors.toList()));
    }

    @GetMapping("/outboxes")
    public ResponseEntity<List<SendMessageListDto>> getSendMessageList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                          @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<SendMessageListDto> sendMessageList = messageService.findAllMessagesBySender(user.getUserId(), pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(sendMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(sendMessageList.stream().collect(Collectors.toList()));
    }
}
