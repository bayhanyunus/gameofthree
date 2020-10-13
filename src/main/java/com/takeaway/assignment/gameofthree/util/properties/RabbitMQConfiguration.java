package com.takeaway.assignment.gameofthree.util.properties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RabbitMQConfiguration {

  private String host;
  private int port;
  private String user;
  private String password;

  private String exchange;
  private String routingKey;
  private String queue;

}
