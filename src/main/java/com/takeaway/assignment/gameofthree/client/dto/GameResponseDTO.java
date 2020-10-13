package com.takeaway.assignment.gameofthree.client.dto;

import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JGlobalMap
@NoArgsConstructor
public class GameResponseDTO {

  private Integer number;

  private String id;

  private Instant createDate;
}
