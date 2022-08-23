package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.BoardService;
import com.stock.yu.downbitbe.board.domain.board.BoardListDto;
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
        return boardService.createBoard(boardCreateDto.get("name"));
    }

    @PatchMapping("/api/v1/boards/{id}")
    public Long updateBoard(@RequestBody Map<String, String> boardUpdateDto, @PathVariable Long id){
        return boardService.updateBoard(boardUpdateDto.get("name"), id);
    }

    @DeleteMapping("/api/v1/boards/{id}")
    public Long deleteBoard(@PathVariable Long id){
        return boardService.deleteBoard(id);
    }

}
