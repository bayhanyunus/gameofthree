package com.takeaway.assignment.gameofthree.service.event;

import com.takeaway.assignment.gameofthree.service.message.GameMessageService;
import com.takeaway.assignment.gameofthree.util.properties.ApplicationConfiguration;
import com.takeaway.assignment.gameofthree.util.properties.PlayMode;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameEventListener {

  @Resource
  private GameMessageService gameMessageService;

  @Resource
  private ApplicationConfiguration applicationConfiguration;

  @EventListener
  public void gameEventListener(final GameEvent event) {

    if (PlayMode.AUTOMATIC == applicationConfiguration.getPlayMode()) {
      log.info("The play move message will be sent to the queue");

      gameMessageService.sendMessageQueue(event.getItem());
    } else {
      log.info("Waiting user interaction to continue to move. Please send a PUT request to play API with id {}", event.getItem().getId());
    }
  }
}