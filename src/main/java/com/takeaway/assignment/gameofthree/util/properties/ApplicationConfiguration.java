package com.takeaway.assignment.gameofthree.util.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;


@Getter
@Setter
@Validated
@ConstructorBinding
@AllArgsConstructor
@ConfigurationProperties(prefix = "takeaway.assignment.gameofthree.service")
public class ApplicationConfiguration {

  private String playerId;

  private PlayMode playMode;

  private NextPlayerConfiguration nextPlayer;

  private RabbitMQConfiguration rabbitMq;

  private RedisConfiguration redis;

}