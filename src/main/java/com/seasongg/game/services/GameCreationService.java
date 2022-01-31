package com.seasongg.game.services;

import com.seasongg.common.SggService;
import com.seasongg.game.models.Game;
import com.seasongg.season.models.SeasonCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameCreationService extends SggService {

    @Autowired
    private GameRepository gameRepository;

    private final Logger LOG = LoggerFactory.getLogger(GameCreationService.class);

    public Game createGame(SeasonCreateRequest seasonCreateRequest) {

        Game bggGame = new Game();

        bggGame.setGameId(seasonCreateRequest.getSeasonGameId());
        bggGame.setGameName(seasonCreateRequest.getSeasonGameName());

        gameRepository.save(bggGame);

        return bggGame;

    }

}
