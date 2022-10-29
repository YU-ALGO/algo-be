package com.stock.yu.downbitbe.message.ui;

import com.stock.yu.downbitbe.message.application.MessageService;
import com.stock.yu.downbitbe.message.domain.MessageCreateRequestDto;
import com.stock.yu.downbitbe.message.domain.MessageDto;
import com.stock.yu.downbitbe.message.domain.ReceiveMessageListDto;
import com.stock.yu.downbitbe.message.domain.SendMessageListDto;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
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

import javax.validation.Valid;
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
                                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                                             @RequestParam(value = "not_read", required = false) Boolean notRead,
                                                                             @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<ReceiveMessageListDto> receiveMessageList = messageService.findAllMessagesByReceiver(pageable, user.getUserId(), notRead, keyword);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(receiveMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(receiveMessageList.stream().collect(Collectors.toList()));
    }

    @GetMapping("/outboxes")
    public ResponseEntity<List<SendMessageListDto>> getSendMessageList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                                          @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Page<SendMessageListDto> sendMessageList = messageService.findAllMessagesBySender(pageable, user.getUserId(), keyword);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(sendMessageList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(sendMessageList.stream().collect(Collectors.toList()));
    }

    @GetMapping("{message_id}")
    public ResponseEntity<MessageDto> getMessage(@PathVariable("message_id") Long messageId,
                                                 @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        MessageDto messageDto = messageService.findMessageByMessageId(messageId, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(messageDto);
    }

    @PostMapping("")
    public ResponseEntity<Long> createMessage(final @RequestBody @Valid MessageCreateRequestDto messageCreateRequestDto,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User receiver = userService.findByNickname(messageCreateRequestDto.getReceiverName());
        User sender = userService.findByUsername(auth.getUsername());
        Long ret = messageService.createMessage(messageCreateRequestDto, receiver, sender);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("{message_id}")
    public ResponseEntity<Long> deleteMessage(@PathVariable("message_id") Long messageId,
                                              @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = messageService.deleteMessage(messageId, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }


//TODO: messageList 삭제하는 거 수정하기

//    @DeleteMapping("")
//    public ResponseEntity<Long> deleteMessageList(@RequestBody List<Long> messageIdArray,
//                                                  @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
//        Long userId = userService.findByUsername(auth.getUsername()).getUserId();
//        Long ret = 0L;
//        for(Long messageId : messageIdArray){
//            ret += messageService.deleteMessage(messageId, userId);
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(ret);
//    }
}
