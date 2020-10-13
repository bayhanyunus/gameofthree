package com.takeaway.assignment.gameofthree.client;

import com.takeaway.assignment.gameofthree.client.dto.GameResponseDTO;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import reactor.core.publisher.Mono;

public interface GameNextPlayerClient {

  Mono<GameResponseDTO> playNextPlayer(GameItem gameItem);

  Mono<Void> finishGameNotification();
}
