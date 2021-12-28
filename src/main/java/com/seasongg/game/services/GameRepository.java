package com.seasongg.game.services;

import com.seasongg.game.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {

}
