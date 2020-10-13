package com.takeaway.assignment.gameofthree.service.message.impl;

import com.googlecode.jmapper.JMapper;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.service.message.GameMessageService;
import com.takeaway.assignment.gameofthree.service.message.dto.GameMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameMessageServiceImpl implements GameMessageService {

  private final JMapper<GameMessage, GameItem> gameItem2GameMessageJMapper;

  private final RabbitTemplate gameRabbitTemplate;

  public GameMessageServiceImpl(RabbitTemplate gameRabbitTemplate) {
    this.gameRabbitTemplate = gameRabbitTemplate;
    gameItem2GameMessageJMapper = new JMapper<>(GameMessage.class, GameItem.class);
  }

  @Override
  public void sendMessageQueue(GameItem item) {

    log.debug("The play move message being sent to the queue");
    final var gameMessage = gameItem2GameMessageJMapper.getDestination(item);

    gameRabbitTemplate.convertAndSend(gameMessage);
    log.info("The play move message was sent to the queue");

  }

}
