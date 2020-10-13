package com.takeaway.assignment.gameofthree.client.impl;

import static com.googlecode.jmapper.api.JMapperAPI.attribute;
import static com.googlecode.jmapper.api.JMapperAPI.mappedClass;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.takeaway.assignment.gameofthree.client.GameNextPlayerClient;
import com.takeaway.assignment.gameofthree.client.dto.GameRequestDTO;
import com.takeaway.assignment.gameofthree.client.dto.GameResponseDTO;
import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.util.properties.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GameNextPlayerClientImpl implements GameNextPlayerClient {

  private final JMapper<GameRequestDTO, GameItem> gameItem2GameRequestJMapper;

  private final WebClient gameNextPlayerWebClient;

  private final String playUri;
  private final String finishGameUri;
  private final HttpMethod httpMethod;
  private final HttpMethod finishGameHttpMethod;

  public GameNextPlayerClientImpl(WebClient gameNextPlayerWebClient,
      ApplicationConfiguration applicationConfiguration) {
    this.gameNextPlayerWebClient = gameNextPlayerWebClient;

    final var nextPlayer = applicationConfiguration.getNextPlayer();

    httpMethod = HttpMethod.valueOf(nextPlayer.getPlay().getHttpMethod());
    playUri = nextPlayer.getPlay().getUri();

    finishGameHttpMethod = HttpMethod.valueOf(nextPlayer.getEndGame().getHttpMethod());
    finishGameUri = nextPlayer.getEndGame().getUri();

    gameItem2GameRequestJMapper = getGameRequestDTOGameItemJMapper();
  }

  private JMapper<GameRequestDTO, GameItem> getGameRequestDTOGameItemJMapper() {

    final JMapperAPI jmapperApi = new JMapperAPI()
        .add(mappedClass(GameItem.class)
            .add(attribute("number").value("number"))
            .add(attribute("id").value("id")));

    return new JMapper<>(GameRequestDTO.class, GameItem.class, jmapperApi);
  }

  @Override
  public Mono<GameResponseDTO> playNextPlayer(GameItem gameItem) {

    log.info("The next player will be callback for id {}", gameItem.getId());

    final var gameRequestDTO = gameItem2GameRequestJMapper.getDestination(gameItem);

    return gameNextPlayerWebClient
        .method(httpMethod)
        .uri(playUri)
        .body(Mono.just(gameRequestDTO), GameRequestDTO.class)
        .retrieve()
        .bodyToMono(GameResponseDTO.class);
  }

  @Override
  public Mono<Void> finishGameNotification() {
    log.info("Game Finish Notification will be sent to next user}");

    return gameNextPlayerWebClient
        .method(finishGameHttpMethod)
        .uri(finishGameUri)
        .retrieve()
        .bodyToMono(Void.class);
  }
}
