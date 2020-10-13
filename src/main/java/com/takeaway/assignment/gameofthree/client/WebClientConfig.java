package com.takeaway.assignment.gameofthree.client;

import com.takeaway.assignment.gameofthree.util.properties.ApplicationConfiguration;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
public class WebClientConfig {

  @Resource
  private ApplicationConfiguration applicationConfiguration;

  @Bean
  public WebClient gameNextPlayerWebClient() {

    final var nextPlayer = applicationConfiguration.getNextPlayer();

    final HttpClient httpClient = HttpClient.create()
        .tcpConfiguration(client ->
            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nextPlayer.getConnectionTimeoutInMs())
                .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(nextPlayer.getReadTimeoutInSeconds()))
                    .addHandlerLast(new WriteTimeoutHandler(nextPlayer.getWriteTimeoutInSeconds()))));

    final ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

    return WebClient
        .builder()//
        .baseUrl(nextPlayer.getBaseUrl())//
        .clientConnector(connector)//
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)//
        .build();
  }


}