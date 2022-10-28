package com.stock.yu.downbitbe.message.domain;

import com.stock.yu.downbitbe.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

    List<Message> findAllBySender(Long id);

}