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

    private final Logger LOG = LoggerFactory.getLogger(GameCreationService.class);

	public static final String GAME_NAME_EXISTS_ERROR_TEXT = "A game with that name already exist. Please use a " +
			"different game name.";

	public static final String GAME_NAME_CONSTRAINT = "games.GAME_NAME_UNIQUE";

    public Game createGame(SeasonCreateRequest seasonCreateRequest) {

        Game bggGame = new Game();

        bggGame.setGameId(seasonCreateRequest.getSeasonGameId());
        bggGame.setGameName(seasonCreateRequest.getSeasonGameName());

        return bggGame;

    }

}
