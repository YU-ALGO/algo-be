package com.stock.yu.downbitbe.board.domain.board;

import com.stock.yu.downbitbe.board.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
