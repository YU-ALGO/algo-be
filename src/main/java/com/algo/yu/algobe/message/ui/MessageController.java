package com.algo.yu.algobe.message.ui;

import com.algo.yu.algobe.message.application.MessageService;
import com.algo.yu.algobe.message.domain.*;
import com.algo.yu.algobe.user.application.UserService;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/messages")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/non_read_counts")
    public ResponseEntity<Integer> getNonReadMessageCount(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        int count = messageService.getNonReadMessageCount(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inboxes")
    public ResponseEntity<List<ReceiveMessageListDto>> getReceiveMessageList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                                             @RequestParam(value = "searchType", required = false) MessageSearchType searchType,
                                                                             @RequestParam(value = "not_read", required = false) Boolean notRead,
                                                                             @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<ReceiveMessageListDto> receiveMessageList = messageService.findAllMessagesByReceiver(pageable, user.getUserId(), notRead, keyword, searchType);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(receiveMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(receiveMessageList.stream().collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/outboxes")
    public ResponseEntity<List<SendMessageListDto>> getSendMessageList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                                       @RequestParam(value = "searchType", required = false) MessageSearchType searchType,
                                                                       @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<SendMessageListDto> sendMessageList = messageService.findAllMessagesBySender(pageable, user.getUserId(), keyword, searchType);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(sendMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(sendMessageList.stream().collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{message_id}")
    public ResponseEntity<MessageDto> getMessage(@PathVariable("message_id") Long messageId,
                                                 @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        MessageDto messageDto = messageService.findMessageByMessageId(messageId, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<Long> createMessage(final @RequestBody @Valid MessageCreateRequestDto messageCreateRequestDto,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User receiver = userService.findByNickname(messageCreateRequestDto.getReceiverName());
        log.info("박세붕id=" + receiver.getUsername() + " " + auth.getUsername());
        if(receiver == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1L);
        else if(Objects.equals(receiver.getUsername(), auth.getUsername())) {
            log.info("박세붕park=");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-2L);
        }
        User sender = userService.findByUsername(auth.getUsername());
        Long ret = messageService.createMessage(messageCreateRequestDto, receiver, sender);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{message_id}")
    public ResponseEntity<Long> deleteMessage(@PathVariable("message_id") Long messageId,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = messageService.deleteMessage(messageId, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/outboxes")
    public ResponseEntity<Long> deleteSenderMessageList(@RequestBody @Valid MessageDeleteRequestDto messageIdArray,
                                                        @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        Long userId = userService.findByUsername(auth.getUsername()).getUserId();
        Long ret = messageService.deleteMessageListBySender(messageIdArray.getMessageIdArray(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/inboxes")
    public ResponseEntity<Long> deleteReceiverMessageList(@RequestBody MessageDeleteRequestDto messageIdArray,
                                                  @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        Long userId = userService.findByUsername(auth.getUsername()).getUserId();
        Long ret = messageService.deleteMessageListByReceiver(messageIdArray.getMessageIdArray(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

}
