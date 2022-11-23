package com.algo.yu.algobe.board.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Boolean existsByBoardName(String boardName);
}
