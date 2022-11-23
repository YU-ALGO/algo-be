package com.algo.yu.algobe.board.domain.board;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BOARD")
@Getter
@NoArgsConstructor
public class Board{
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "board_name")
    @NotNull
    private String boardName;

    public Board(String name){
        this.boardName = name;
    }

    public Board update(String name) {
        if(name != null && !name.isEmpty())
            this.boardName = name;
        return this;
    }
}
