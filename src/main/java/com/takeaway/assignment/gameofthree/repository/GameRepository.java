package com.takeaway.assignment.gameofthree.repository;

import com.takeaway.assignment.gameofthree.repository.model.GameItem;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GameRepository extends PagingAndSortingRepository<GameItem, String> {

}