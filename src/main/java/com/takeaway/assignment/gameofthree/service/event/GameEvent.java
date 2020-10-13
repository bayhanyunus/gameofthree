package com.takeaway.assignment.gameofthree.service.event;

import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import com.takeaway.assignment.gameofthree.util.event.BaseGameEvent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameEvent implements BaseGameEvent {

  private final GameItem item;

}