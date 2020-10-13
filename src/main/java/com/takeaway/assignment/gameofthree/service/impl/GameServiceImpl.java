package com.takeaway.assignment.gameofthree.service.impl;

import com.takeaway.assignment.gameofthree.repository.GameRepository;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.service.event.GameEvent;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service("gameService")
public class GameServiceImpl implements GameService {

  private final GameRepository gameRepository;
  private final ApplicationEventPublisher applicationEventPublisher;

  public GameServiceImpl(GameRepository gameRepository, ApplicationEventPublisher applicationEventPublisher) {
    this.gameRepository = gameRepository;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Mono<GameItem> accept(GameItem gameItem) {

    return Mono.just(gameRepository.save(gameItem)).doOnNext(
        ignored -> {
          log.info("Game request is saved {}", gameItem.getId());

          if (log.isDebugEnabled()) {
            log.debug("Game request is saved {}", gameItem.toString());
          }
        })
        .doOnNext(ignored -> log.info("Game request event will be published for id {} ", gameItem.getId()))
        .doOnNext(savedItem -> applicationEventPublisher.publishEvent(GameEvent.builder().item(savedItem).build()));
  }

  @Override
  public Mono<GameItem> play(final GameItem gameItem) {

    return Mono.just(gameItem)
        .doOnNext(ignored -> log.debug("Game item is reached to actual play service {}", gameItem.toString()))
        .map(this::playInternal)
        .doOnSuccess(ignored -> log.info("Game is played for id {}", gameItem.getId()))
        .doOnError(error -> log.error("While playing game error occurred", error));
  }

  private GameItem playInternal(GameItem gameItem) {
    log.info("The real play is playing for id {} ", gameItem.getId());
    log.debug("Calculating... old number {}", gameItem.getNumber());

    var number = gameItem.getNumber();

    final var remain = number % 3; // 0 1 2
    if (remain > 0) {
      var add = 1 == remain ? -1 : +1;
      number += add;
    }
    number = number / 3;

    gameItem.setNumber(number);
    log.debug("Calculated... new number {}", gameItem.getNumber());

    return gameItem;
  }

  @Override
  public Mono<Boolean> isNotPlayed(String id) {
    return Mono.just(StringUtils.isEmpty(id) ? gameRepository.findAll().iterator().hasNext() : find(id).isEmpty());
  }

  @Override
  public Optional<GameItem> find(final String id) {
    return gameRepository.findById(id);
  }

  @Override
  public void releaseMovement() {
    log.info("Game is ended");
    gameRepository.deleteAll();
  }
}
