package com.takeaway.assignment.gameofthree.repository.model;

import com.googlecode.jmapper.annotations.JMap;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@RedisHash
public class GameItem {

  @JMap
  @Id
  private String id;

  @JMap
  private Integer number;

  @JMap
  private Instant createDate = Instant.now();

}
