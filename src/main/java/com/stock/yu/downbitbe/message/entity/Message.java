package com.stock.yu.downbitbe.message.entity;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE")
@Getter
@NoArgsConstructor
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column(name = "read_time", updatable = false)
    private LocalDateTime readTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY) //LAZY는 연결된 내용은 나중에 검색, EAGER는 한번에 가져옴
    @NotNull
    private User sender;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User receiver;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private DeleteCondition deleted; // delete 는 mysql 예약어

    @Builder
    public Message(String title, String content, LocalDateTime readTime, User sender, User receiver, DeleteCondition deleted){
        this.title = title;
        this.content = content;
        this.readTime = readTime;
        this.sender = sender;
        this.receiver = receiver;
        this.deleted = deleted;
    }

}
