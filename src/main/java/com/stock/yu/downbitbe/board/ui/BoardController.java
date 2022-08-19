package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.BoardService;
import com.stock.yu.downbitbe.board.domain.BoardListDto;
import com.stock.yu.downbitbe.board.domain.PostListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/v1/boards")
    public List<BoardListDto> boards(){
        return boardService.findAllBoards();
    }

    @PostMapping("/api/v1/boards")
    public Long createBoard(@RequestBody Map<String, String> boardCreateDto){
        return boardService.createBoard(boardCreateDto);
    }

}
