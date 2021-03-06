package com.seasongg.game.services;

import com.seasongg.game.models.Game;
import com.seasongg.season.models.SeasonCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameCreationServiceTest {

	@InjectMocks
	private GameCreationService gameCreationService;

	@Test
	public void should_CreateGame_When_InputOK() {
		// given
		SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest();
		seasonCreateRequest.setSeasonGameId(1);
		seasonCreateRequest.setSeasonGameName("Test Game");

		Game expectedGame = new Game();
		expectedGame.setGameId(1);
		expectedGame.setGameName("Test Game");

		// when
		Game actualGame = gameCreationService.createGame(seasonCreateRequest);

		//then
		assertEquals(expectedGame, actualGame);

	}

}