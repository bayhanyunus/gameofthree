package com.takeaway.assignment.gameofthree.util.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NextPlayerConfiguration {

  private String baseUrl;

  private int connectionTimeoutInMs;
  private int readTimeoutInSeconds;
  private int writeTimeoutInSeconds;

  private Endpoint play;
  private Endpoint endGame;

  @Getter
  @Setter
  public static class Endpoint {

    private String uri;
    private String httpMethod;
  }

}
