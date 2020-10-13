package com.takeaway.assignment.gameofthree.service.message;

import com.googlecode.jmapper.JMapper;
import com.takeaway.assignment.gameofthree.client.GameNextPlayerClient;
import com.takeaway.assignment.gameofthree.client.dto.GameResponseDTO;
import com.takeaway.assignment.gameofthree.client.dto.GameResumeRequestDTO;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.service.message.dto.GameMessage;
import com.takeaway.assignment.gameofthree.util.ApplicationConstant;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GameMessageListener {

  private final GameService gameService;
  private final GameNextPlayerClient gameNextPlayerClient;
  private final JMapper<GameItem, GameMessage> gameItem2gameResponseDTOJMapper;

  public GameMessageListener(GameService gameService, GameNextPlayerClient gameNextPlayerClient) {
    this.gameService = gameService;
    this.gameNextPlayerClient = gameNextPlayerClient;
    gameItem2gameResponseDTOJMapper = new JMapper<>(GameItem.class, GameMessage.class);
  }

  @RabbitListener(queues = "${takeaway.assignment.gameofthree.service.rabbit-mq.queue}")
  public void handle(final GameMessage message) {
    log.info("The play message is received from the queue. The item id {}", message.getId());

    Mono.just(gameItem2gameResponseDTOJMapper.getDestination(message))
        .flatMap(gameService::play)
        .flatMap(gameItem -> {

          if (gameItem.getNumber() > 0 && ApplicationConstant.WINNER_NUMBER != gameItem.getNumber()) {
            return gameNextPlayerClient.playNextPlayer(gameItem);
          } else {
            log.info("The player is won");
            return gameNextPlayerClient.finishGameNotification()
                .doOnNext(ignored -> gameService.releaseMovement());
          }
        })
        .doOnError(error -> log.error("The error is occurred while calling the next player ", error))
        .subscribe();
  }
}
