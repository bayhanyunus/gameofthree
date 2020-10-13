package com.takeaway.assignment.gameofthree.controller.dto;

import com.googlecode.jmapper.annotations.JMap;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class GameResponseDTO {
  @JMap
  private Integer number;
  @JMap
  private String id;
  @JMap
  private Instant createDate;
}
