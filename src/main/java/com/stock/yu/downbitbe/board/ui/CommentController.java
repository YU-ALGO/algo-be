package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.CommentService;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CustomUserRepository userRepository;
}
