package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.BoardListDto;
import com.stock.yu.downbitbe.board.domain.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

//    public Long createBoard(Map<String, String> boardCreateDto) {   // security admin 확인 필요
//    }
}
