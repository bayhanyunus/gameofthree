package com.takeaway.assignment.gameofthree.controller.game;

import static com.googlecode.jmapper.api.JMapperAPI.attribute;
import static com.googlecode.jmapper.api.JMapperAPI.mappedClass;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.takeaway.assignment.gameofthree.client.dto.GameResumeRequestDTO;
import com.takeaway.assignment.gameofthree.controller.dto.GameRequestDTO;
import com.takeaway.assignment.gameofthree.controller.dto.GameResponseDTO;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.service.GameService;
import com.takeaway.assignment.gameofthree.service.message.GameMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/game")
public class GameController {

  private final GameService gameService;
  private final GameMessageService gameMessageService;

  private final JMapper<GameResponseDTO, GameItem> gameItem2gameResponseDTOJMapper;
  private final JMapper<GameItem, GameRequestDTO> gameRequestDTO2GameItemJMapper;

  public GameController(final GameService gameService, GameMessageService gameMessageService) {

    this.gameService = gameService;
    this.gameMessageService = gameMessageService;

    gameItem2gameResponseDTOJMapper = new JMapper<>(GameResponseDTO.class, GameItem.class);
    gameRequestDTO2GameItemJMapper = getGameRequestDTOGameItemJMapper();
  }

  private JMapper<GameItem, GameRequestDTO> getGameRequestDTOGameItemJMapper() {

    final JMapperAPI jmapperApi = new JMapperAPI()
        .add(mappedClass(GameRequestDTO.class)
            .add(attribute("id").value("id"))
            .add(attribute("number").value("number")));

    return new JMapper<>(GameItem.class, GameRequestDTO.class, jmapperApi);
  }

  @PostMapping("/play")
  @ResponseBody()
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<GameResponseDTO> play(@RequestBody GameRequestDTO gameRequestDTO) {

    log.info("Game request is reached to player");

    return gameService.isNotPlayed(gameRequestDTO.getId()).filter(b -> !b)
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.TOO_EARLY, "Game is not over. Please continue existing game or wait your turn")))//
        .map(ignored -> gameRequestDTO2GameItemJMapper.getDestination(gameRequestDTO))//
        .doOnNext(ignored -> log.debug("Game request is converted to service layer object"))//
        .doOnNext(gameItem -> log.info("Game request {} will be accepted by player", gameItem.getNumber()))
        .flatMap(gameService::accept)
        .map(gameItem2gameResponseDTOJMapper::getDestination)
        .doOnError(error -> log.error("The error is occurred while playing ", error));
  }

  @ResponseBody()
  @PutMapping("/play")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<GameResponseDTO> resume(@RequestBody GameResumeRequestDTO gameResumeRequestDTO) {

    return Mono.just(gameService.find(gameResumeRequestDTO.getId())
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movement does not exist in the system with given id" + gameResumeRequestDTO.getId())))
        .doOnNext(gameItem -> log.info("Game request {} will be played by player", gameItem.getId()))
        .doOnNext(gameMessageService::sendMessageQueue)
        .map(gameItem2gameResponseDTOJMapper::getDestination)
        .doOnError(error -> log.error("The error is occurred while playing ", error));
  }

  @DeleteMapping("/endgame")
  @ResponseStatus(HttpStatus.OK)
  public Mono<Void> endGame() {
    log.info("The other player is won");
    gameService.releaseMovement();
    return Mono.empty();
  }
}
