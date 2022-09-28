package com.stock.yu.downbitbe.board.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardListDto {
    private Long id;
    private String name;

    public BoardListDto(Board board){
        this.id = board.getBoardId();
        this.name = board.getBoardName();
    }
}
