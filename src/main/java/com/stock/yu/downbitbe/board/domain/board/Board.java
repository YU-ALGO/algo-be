package com.stock.yu.downbitbe.board.domain.board;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "BOARD")
@Getter
@NoArgsConstructor
public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @Builder
    public Board(String name){
        this.name = name;
    }

    public Board update(String name) {
        if(name != null && !name.isEmpty())
            this.name = name;
        return this;
    }
}
