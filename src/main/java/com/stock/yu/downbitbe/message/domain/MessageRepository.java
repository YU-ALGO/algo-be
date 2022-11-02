package com.stock.yu.downbitbe.message.domain;

import com.stock.yu.downbitbe.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("update Message set deleted = :condition where messageId = :messageId")
    int updateDeleteCondition(@Param(value = "messageId") Long messageId, @Param(value = "condition") DeleteCondition condition);

}
