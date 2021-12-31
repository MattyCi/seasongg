package com.seasongg.season.services;

import com.seasongg.common.SggService;
import com.seasongg.game.models.Game;
import com.seasongg.game.services.GameCreationService;
import com.seasongg.game.services.GameRepository;
import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.season.models.SeasonCreateResponse;
import com.seasongg.season.models.SeasonStatus;
import com.seasongg.user.models.permissions.Permission;
import com.seasongg.user.models.permissions.UserPermission;
import com.seasongg.user.services.permissions.PermissionRepository;
import com.seasongg.user.services.permissions.UserPermissionRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class SeasonCreateService extends SggService {

    @Autowired
    SeasonRepository seasonRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserPermissionRepository userPermissionRepository;

    @Autowired
    GameCreationService gameCreationService;

    private final Logger LOG = LoggerFactory.getLogger(SeasonCreateService.class);

    private static final String SEASON_DATE_FORMAT = "MM/dd/yyyy";

    private static final String SEASON_NAME_TOO_SHORT_ERROR_TEXT = "Season names must be at least 4 characters long.";
    private static final String SEASON_NAME_TOO_LONG_ERROR_TEXT = "Season names cannot be more than 56 " +
            "characters long.";
    private static final String SEASON_NAME_EXISTS_ERROR_TEXT = "Sorry, but the season name you provided " +
            "already exists.";

    private static final String INVALID_END_DATE_ERROR_TEXT = "The season end date provided was invalid, please "
            + "choose a valid date with the format mm/dd/yyyy and try again.";
    private static final String END_DATE_BEFORE_START_ERROR_TEXT = "Please choose a date in the future for your " +
            "season end date.";

    public static final String SCORING_TYPE_EMPTY = "Please provide a season scoring type.";

    private static final String NO_GAME_SELECTED_ERROR_TEXT = "Please select a game to be played " +
            "throughout the season.";
    private static final String UNEXPECTED_GAME_ERROR = "Sorry, we had an issue finding the selected game. " +
            "Please try again.";

    @Transactional(rollbackFor = Exception.class)
    public SeasonCreateResponse createSeason(SeasonCreateRequest seasonCreateRequest) throws Exception {

            validateSeasonName(seasonCreateRequest.getSeasonName());
            validateSeasonDates(seasonCreateRequest.getSeasonEndDate());
            validateGameId(seasonCreateRequest.getSeasonGameId());

            Season season = buildSeason(seasonCreateRequest);

            return new SeasonCreateResponse(season);
    }

    private void validateSeasonName(String seasonName) throws SeasonCreateException {

        if (seasonName == null || seasonName.isEmpty()) {
            throw new SeasonCreateException(SEASON_NAME_TOO_SHORT_ERROR_TEXT);
        }

        seasonName = seasonName.trim();

        if (seasonName.length() < 4) {
            throw new SeasonCreateException(SEASON_NAME_TOO_SHORT_ERROR_TEXT);
        } else if (seasonName.length() > 56) {
            throw new SeasonCreateException(SEASON_NAME_TOO_LONG_ERROR_TEXT);
        }

    }

    private void validateSeasonDates(String seasonEndDate) throws SeasonCreateException {

        if (seasonEndDate == null || seasonEndDate.isEmpty()) {
            throw new SeasonCreateException(INVALID_END_DATE_ERROR_TEXT);
        }

        Timestamp seasonEndTimestamp;
        Timestamp seasonStartTimestamp;

        try {
            seasonEndTimestamp = getTimestamp(seasonEndDate);
            seasonStartTimestamp = new Timestamp(System.currentTimeMillis());
        } catch (Exception e) {
            LOG.error("unexpected error validating season end date for user {} and end date {}.", getUsername(),
                    seasonEndDate, e);
            throw new SeasonCreateException(INVALID_END_DATE_ERROR_TEXT);
        }

        if (seasonEndTimestamp.before(seasonStartTimestamp)) {

            throw new SeasonCreateException(END_DATE_BEFORE_START_ERROR_TEXT);

        }

    }

    private Timestamp getTimestamp(String seasonEndDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SEASON_DATE_FORMAT);
        Date parsedDate = dateFormat.parse(seasonEndDate);

        return new Timestamp(parsedDate.getTime());
    }

    private void validateGameId(Integer seasonGameId) throws SeasonCreateException {

        if (seasonGameId == null) {

            LOG.info("no game selected when user {} tried creating season.", getUsername());

            throw new SeasonCreateException(NO_GAME_SELECTED_ERROR_TEXT);
        }

    }


    public Season buildSeason(SeasonCreateRequest seasonCreateRequest) throws Exception {

        Optional<Game> bggGame = gameRepository.findById(seasonCreateRequest.getSeasonGameId());

        if (bggGame.isEmpty()) {
            bggGame = Optional.ofNullable(gameCreationService.createGame(seasonCreateRequest));
        }

        Season season = new Season();
        season.setName(seasonCreateRequest.getSeasonName());

        if (bggGame.isPresent()) {
            season.setGame(bggGame.get());
        } else {
            throw new SeasonCreateException(UNEXPECTED_GAME_ERROR);
        }

        season.setStartDate(new Timestamp(System.currentTimeMillis()));
        season.setEndDate(getTimestamp(seasonCreateRequest.getSeasonEndDate()));

        if (seasonCreateRequest.getSeasonScoringType() == null ||
                "".equals(seasonCreateRequest.getSeasonScoringType())) {
            LOG.info("season scoring type was not provided by {} when creating season.",  getUsername());
            throw new SeasonCreateException(SCORING_TYPE_EMPTY);
        }

        season.setScoringType(seasonCreateRequest.getSeasonScoringType());

        season.setStatus(SeasonStatus.ACTIVE.toString());

        // TODO: make sure the anonymous user or whatever cannot create seasons
        season.setCreator(getExecutingUser());

        try {
            seasonRepository.save(season);
        } catch (Exception e) {
            if (e.getCause() instanceof  ConstraintViolationException)
                throw new SeasonCreateException(SEASON_NAME_EXISTS_ERROR_TEXT);
            throw e;
        }

        try {
            assignUserPermissionsForSeason(season);
        } catch (Exception e) {
            LOG.error("Unexpected error occurred when assigning new season admin permissions.", e);
            throw e;
        }

        return season;
    }

    private void assignUserPermissionsForSeason(Season season) {

        Permission permission = createPermission(season);
        associateUserToPermission(season, permission);

    }

    private Permission createPermission(Season season) {
        Permission permission;
        permission = new Permission();
        permission.setPermValue("season:*:"+season.getSeasonId());
        permissionRepository.save(permission);
        return permission;
    }

    private void associateUserToPermission(Season season, Permission permission) {

        LOG.debug("granting user {} admin rights for season {}.", getUsername(), season.getSeasonId());

        UserPermission userPermission = new UserPermission();
        userPermission.setPermission(permission);
        userPermission.setReguser(season.getCreator());
        userPermissionRepository.save(userPermission);

    }

    public static class SeasonCreateException extends SggServiceException {
        public SeasonCreateException(String message) {
            super(message);
        }
    }

}
