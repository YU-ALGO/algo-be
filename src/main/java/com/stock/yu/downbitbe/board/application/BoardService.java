package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardListDto;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardListDto> findAllBoards(){
        return boardRepository.findAll().stream()
                .map(BoardListDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public String findBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return board.getName();
    }

    @Transactional
    public Long createBoard(String name) {   // security admin 확인 필요
        return boardRepository.save(Board.builder()
                .name(name).build()).getId();
    }

    @Transactional
    public Long updateBoard(String name, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return boardRepository.save(board.update(name)).getId();
    }

    @Transactional
    public Long deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        boardRepository.delete(board);
        return board.getId();
    }
}
