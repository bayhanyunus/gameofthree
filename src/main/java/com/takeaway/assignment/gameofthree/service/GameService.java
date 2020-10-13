package com.takeaway.assignment.gameofthree.service;

import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface GameService {

  Mono<GameItem> accept(GameItem gameItem);

  Mono<GameItem> play(GameItem gameItem);

  Mono<Boolean> isNotPlayed(String id);

  Optional<GameItem> find(String id);

  void releaseMovement();

}
