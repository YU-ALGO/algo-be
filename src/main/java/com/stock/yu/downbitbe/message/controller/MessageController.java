package com.stock.yu.downbitbe.message.controller;

import com.stock.yu.downbitbe.message.dto.MessageDTO;
import com.stock.yu.downbitbe.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/api/v1/messages/")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody MessageDTO messageDTO) {
        log.info("--------------------register------------------");
        log.info(messageDTO);

        Long id = messageService.register(messageDTO);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
