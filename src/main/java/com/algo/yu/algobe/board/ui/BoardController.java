package com.algo.yu.algobe.board.ui;

import com.algo.yu.algobe.board.application.BoardService;
import com.algo.yu.algobe.board.domain.board.BoardListDto;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<BoardListDto>> getBoardList(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        List<BoardListDto> boardListDto = boardService.findAllBoards();

        return ResponseEntity.status(HttpStatus.OK).body(boardListDto);
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<String> getBoard(@PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        String boardName = boardService.findBoardById(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Long> createBoard(@RequestBody Map<String, String> boardCreateDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        String boardName = boardCreateDto.get("name");
        if(boardName.isBlank() || boardService.findBoardByBoardName(boardName))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        Long createBoardId = boardService.createBoard(boardName);
        if(createBoardId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1L); //TODO 에러코드 다시 살펴보기
        }
        return ResponseEntity.status(HttpStatus.OK).body(createBoardId);    //TODO 추후 수정
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{board_id}")
    public ResponseEntity<Long> updateBoard(@RequestBody Map<String, String> boardUpdateDto, @PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        Long updateBoardId = boardService.updateBoard(boardUpdateDto.get("name"), boardId);
        return ResponseEntity.status(HttpStatus.OK).body(updateBoardId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{board_id}")
    public ResponseEntity<Long> deleteBoard(@PathVariable("board_id") Long boardId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        Long deleteBoardId = boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(deleteBoardId);
    }

}
