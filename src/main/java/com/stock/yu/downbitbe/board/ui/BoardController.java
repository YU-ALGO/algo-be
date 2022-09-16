package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.BoardService;
import com.stock.yu.downbitbe.board.domain.board.BoardListDto;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/api/v1/boards")
    public ResponseEntity<List<BoardListDto>> boards(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        List<BoardListDto> boardListDto = boardService.findAllBoards();

        return ResponseEntity.status(HttpStatus.OK).body(boardListDto);
    }

    @GetMapping("/api/v1/boards/{board_id}")
    public ResponseEntity<String> board(@PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        String boardName = boardService.findBoardById(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardName);
    }

    @PostMapping("/api/v1/boards")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBoard(@RequestBody Map<String, String> boardCreateDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long createBoardId = boardService.createBoard(boardCreateDto.get("name"));
        if(createBoardId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request"); // 에러코드 다시 살펴보기
        }
        return ResponseEntity.status(HttpStatus.OK).body(createBoardId);    // 추후 수정
    }

    @PatchMapping("/api/v1/boards/{board_id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBoard(@RequestBody Map<String, String> boardUpdateDto, @PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long updateBoardId = boardService.updateBoard(boardUpdateDto.get("name"), boardId);
        return ResponseEntity.status(HttpStatus.OK).body(updateBoardId);
    }

    @DeleteMapping("/api/v1/boards/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long deleteBoardId = boardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleteBoardId);
    }

}
