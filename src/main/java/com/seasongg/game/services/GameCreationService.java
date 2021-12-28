package com.seasongg.game.services;

import com.seasongg.common.SggService;
import com.seasongg.game.models.Game;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.user.models.RegistrationResponse;
import com.seasongg.user.models.Reguser;
import com.seasongg.user.services.ReguserRepository;
import com.seasongg.user.services.UserBuilder;
import com.seasongg.user.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

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
