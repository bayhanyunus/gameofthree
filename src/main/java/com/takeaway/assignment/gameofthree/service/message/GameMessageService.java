package com.takeaway.assignment.gameofthree.service.message;

import com.takeaway.assignment.gameofthree.repository.model.GameItem;

public interface GameMessageService {

  void sendMessageQueue(GameItem item);
}
