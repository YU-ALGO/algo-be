package com.stock.yu.downbitbe.message.repository;

import com.stock.yu.downbitbe.message.entity.Message;
import com.stock.yu.downbitbe.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @EntityGraph(attributePaths = "sender", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Message m where m.sender.id =:id")
    Optional<Message> getWithSender(Long id);

    @EntityGraph(attributePaths = {"sender"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Message m where m.sender.id =:senderId")
    List<Message> getList(Long senderId);
}
