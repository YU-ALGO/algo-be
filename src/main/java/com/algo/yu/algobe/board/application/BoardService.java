package com.algo.yu.algobe.board.application;

import com.algo.yu.algobe.board.domain.board.Board;
import com.algo.yu.algobe.board.domain.board.BoardListDto;
import com.algo.yu.algobe.board.domain.board.BoardRepository;
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
        return board.getBoardName();
    }

    @Transactional(readOnly = true)
    public Boolean findBoardByBoardName(String boardName){
        return boardRepository.existsByBoardName(boardName);
    }

    @Transactional
    public Long createBoard(String name) {   //TODO : security admin 확인 필요
        return boardRepository.save(new Board(name)).getBoardId();
    }

    @Transactional
    public Long updateBoard(String name, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return boardRepository.save(board.update(name)).getBoardId();
    }

    @Transactional
    public Long deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        boardRepository.delete(board);
        return board.getBoardId();
    }
}
