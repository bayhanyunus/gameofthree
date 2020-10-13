package com.takeaway.assignment.gameofthree.service.message.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GameMessage implements Serializable {

  private String id;
  private Integer number;
  private Instant createDate;

  private final Instant messageDate = Instant.now();

}