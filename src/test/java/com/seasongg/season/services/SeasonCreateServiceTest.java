package com.seasongg.season.services;

import com.seasongg.common.SggService;
import com.seasongg.game.models.Game;
import com.seasongg.game.services.GameCreationService;
import com.seasongg.game.services.GameRepository;
import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.user.models.Reguser;
import com.seasongg.user.services.permissions.PermissionRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.LockTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeasonCreateServiceTest {

    @Spy
    @InjectMocks
    private SeasonCreateService seasonCreateService;

    @Mock
    GameRepository gameRepository;

    @Mock
    SeasonRepository seasonRepository;

    @Mock
    GameCreationService gameCreationService;

    @Mock
    PermissionRepository permissionRepository;

    @BeforeEach
    void setup() throws SggService.SggServiceException {

        lenient().doReturn("test-user").when(seasonCreateService).getUsername();
        lenient().doReturn(mock(Reguser.class)).when(seasonCreateService).getExecutingUser();

    }

    @Test
    void should_EnsureValidationMethodsAreRun() throws Exception {
        // given
        doNothing().when(seasonCreateService).validateSeasonName(anyString());
        doNothing().when(seasonCreateService).validateSeasonDates(anyString(), eq(false));
        doNothing().when(seasonCreateService).validateGameId(anyInt());
        doReturn(new Season()).when(seasonCreateService).buildSeason(any());
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        // when
        seasonCreateService.createSeason(seasonCreateRequest);

        // then
        verify(seasonCreateService).validateSeasonName(anyString());
        verify(seasonCreateService).validateSeasonDates(anyString(), eq(false));
        verify(seasonCreateService).validateGameId(anyInt());
        verify(seasonCreateService).buildSeason(any());

    }

    @Test
    void should_BuildSeason_When_InputOK() throws Exception {
        // given
        Game expectedGame = new Game();
        expectedGame.setGameId(1);
        expectedGame.setGameName("test-game");
        given(gameRepository.findById(1)).willReturn(Optional.of(expectedGame));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                expectedGame.getGameId(),
                "game-name",
                "01/01/3000",
                "A"
        );
        Reguser seasonCreator = new Reguser();
        seasonCreator.setUsername("test-user");
        doReturn(seasonCreator).when(seasonCreateService).getExecutingUser();

        long janFirstThreeThousand = 32503698000000L;
        Timestamp expectedEndDate = new Timestamp(janFirstThreeThousand);
        doReturn(expectedEndDate).when(seasonCreateService).getTimestamp(seasonCreateRequest.getSeasonEndDate());

        doNothing().when(seasonCreateService).assignUserPermissionsForSeason(any(Season.class));

        Season expectedSeason = new Season();
        expectedSeason.setCreator(seasonCreator);
        expectedSeason.setStatus("A");
        expectedSeason.setScoringType(seasonCreateRequest.getSeasonScoringType());
        expectedSeason.setGame(expectedGame);
        expectedSeason.setEndDate(expectedEndDate);
        expectedSeason.setName(seasonCreateRequest.getSeasonName());

        // when
        Season actualSeason = seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        expectedSeason.setStartDate(actualSeason.getStartDate()); // must be done manually since start time is uniquely generated inside buildSeason()
        assertEquals(expectedSeason, actualSeason);

    }

    @Test
    void should_PersistSeason_When_InputOK() throws Exception {
        // given
        Game expectedGame = new Game();
        expectedGame.setGameId(1);
        expectedGame.setGameName("test-game");
        given(gameRepository.findById(1)).willReturn(Optional.of(expectedGame));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                expectedGame.getGameId(),
                "game-name",
                "01/01/3000",
                "A"
        );
        Reguser seasonCreator = new Reguser();
        seasonCreator.setUsername("test-user");
        doReturn(seasonCreator).when(seasonCreateService).getExecutingUser();

        long janFirstThreeThousand = 32503698000000L;
        Timestamp expectedEndDate = new Timestamp(janFirstThreeThousand);
        doReturn(expectedEndDate).when(seasonCreateService).getTimestamp(seasonCreateRequest.getSeasonEndDate());

        doNothing().when(seasonCreateService).assignUserPermissionsForSeason(any(Season.class));

        // when
        seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        verify(seasonRepository).save(any(Season.class));

    }

    @Test
    void should_CreateGame_When_GivenGameDoesNotExist() throws Exception {
        // given
        Optional<Game> emptyGame = Optional.empty();
        given(gameRepository.findById(1)).willReturn(emptyGame);

        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        given(gameCreationService.createGame(seasonCreateRequest)).willReturn(mock(Game.class));

        doNothing().when(seasonCreateService).assignUserPermissionsForSeason(any(Season.class));

        // when
        seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        verify(gameCreationService).createGame(seasonCreateRequest);

    }

    @Test
    void should_ThrowException_When_ScoringTypeNull() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(new Game()));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                null
        );

        // when
        Executable executable = () -> seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        assertThrows(SeasonService.SeasonException.class, executable);

    }

    @Test
    void should_ThrowException_When_ScoringTypeEmpty() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(new Game()));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                ""
        );

        // when
        Executable executable = () -> seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        assertThrows(SeasonService.SeasonException.class, executable);

    }

    @Test
    void should_ThrowException_When_SeasonNameAlreadyExists() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(new Game()));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        Exception testException = new RuntimeException("test", mock(ConstraintViolationException.class));
        doThrow(testException).when(seasonRepository).save(any(Season.class));

        // when
        Exception exception = assertThrows(SeasonService.SeasonException.class, () -> {
            seasonCreateService.buildSeason(seasonCreateRequest);
        });

        // then
        assertThat(exception.getMessage(), containsString("the season name you provided already exists"));

    }

    @Test
    void should_ThrowException_When_UnexpectedPersistenceErrorOccurs() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(new Game()));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        doThrow(LockTimeoutException.class).when(seasonRepository).save(any(Season.class));

        // when
        Executable executable = () -> seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        assertThrows(LockTimeoutException.class, executable);

    }

    @Test
    void should_ThrowException_When_UnexpectedPermissionsPersistenceErrorOccurs() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.of(new Game()));
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        doThrow(DataException.class).when(seasonCreateService).assignUserPermissionsForSeason(any(Season.class));

        // when
        Executable executable = () -> seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        assertThrows(DataException.class, executable);

    }

    @Test
    void should_ThrowException_When_UnexpectedGameCreationErrorOccurs() {
        // given
        given(gameRepository.findById(1)).willReturn(Optional.empty());
        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );
        given(gameCreationService.createGame(seasonCreateRequest)).willReturn(null);

        // when
        Executable executable = () -> seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        assertThrows(SeasonService.SeasonException.class, executable);

    }

    @Test
    // TODO: implement this
    void should_AssignUserPermissionsUponSeasonCreate() throws Exception {
        // given
        Optional<Game> emptyGame = Optional.empty();
        given(gameRepository.findById(1)).willReturn(emptyGame);

        SeasonCreateRequest seasonCreateRequest = new SeasonCreateRequest(
                "season-name",
                1,
                "game-name",
                "01/01/3000",
                "A"
        );

        given(gameCreationService.createGame(seasonCreateRequest)).willReturn(mock(Game.class));

        doNothing().when(seasonCreateService).assignUserPermissionsForSeason(any(Season.class));

        // when
        seasonCreateService.buildSeason(seasonCreateRequest);

        // then
        verify(gameCreationService).createGame(seasonCreateRequest);

    }

}