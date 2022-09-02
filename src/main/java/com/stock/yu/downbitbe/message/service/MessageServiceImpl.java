package com.stock.yu.downbitbe.domain.message.service;

import com.stock.yu.downbitbe.domain.message.dto.MessageDTO;
import com.stock.yu.downbitbe.domain.message.entity.Message;
import com.stock.yu.downbitbe.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Long register(MessageDTO messageDTO) {
        Message message = dtoToEntity(messageDTO);

        log.info("===============================");
        log.info(message);

        messageRepository.save(message);

        return message.getId();
    }

    @Override
    public MessageDTO get(Long id) {
        Optional<Message> result = messageRepository.getWithSender(id);

        if(result.isPresent())
            return entityToDTO(result.get());
        return null;
    }

    @Override
    public void remove(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public List<MessageDTO> getAllWithWriter(Long senderId) {
        List<Message> messageList = messageRepository.getList(senderId);

        return messageList.stream().map(message -> entityToDTO(message)).collect(Collectors.toList());
    }
}
