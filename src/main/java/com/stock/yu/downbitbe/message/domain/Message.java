package com.stock.yu.downbitbe.message.domain;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Message extends BaseTimeEntity {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column(name = "read_time")
    private LocalDateTime readTime;

    @Column(name = "is_read", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean isRead;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY) //LAZY는 연결된 내용은 나중에 검색, EAGER는 한번에 가져옴
    @NotNull
    private User sender;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User receiver;

    @Column(columnDefinition = "varchar(32) default 'NONE'")
    @Enumerated(EnumType.STRING)
    @NotNull
    private DeleteCondition deleted; // delete 는 mysql 예약어

    @Builder
    public Message(String title, String content, User sender, User receiver){
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message updateRead(){
        this.readTime = LocalDateTime.now();
        this.isRead = true;
        return this;
    }
}
