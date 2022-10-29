package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.BoardService;
import com.stock.yu.downbitbe.board.domain.board.BoardListDto;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class BoardController {
    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<List<BoardListDto>> getBoardList(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        List<BoardListDto> boardListDto = boardService.findAllBoards();

        return ResponseEntity.status(HttpStatus.OK).body(boardListDto);
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<String> getBoard(@PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        String boardName = boardService.findBoardById(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardName);
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createBoard(@RequestBody Map<String, String> boardCreateDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long createBoardId = boardService.createBoard(boardCreateDto.get("name"));
        if(createBoardId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1L); // 에러코드 다시 살펴보기
        }
        return ResponseEntity.status(HttpStatus.OK).body(createBoardId);    // 추후 수정
    }

    @PatchMapping("/{board_id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> updateBoard(@RequestBody Map<String, String> boardUpdateDto, @PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long updateBoardId = boardService.updateBoard(boardUpdateDto.get("name"), boardId);
        return ResponseEntity.status(HttpStatus.OK).body(updateBoardId);
    }

    @DeleteMapping("/{board_id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> deleteBoard(@PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        Long deleteBoardId = boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteBoardId);
    }

}
