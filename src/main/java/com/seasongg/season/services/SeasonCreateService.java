package com.seasongg.season.services;

import com.seasongg.game.models.Game;
import com.seasongg.game.services.GameCreationService;
import com.seasongg.game.services.GameRepository;
import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.season.models.SeasonResponse;
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

import java.util.Optional;

@Service
public class SeasonCreateService extends SeasonService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    UserPermissionRepository userPermissionRepository;

    @Autowired
    GameCreationService gameCreationService;

    private final Logger LOG = LoggerFactory.getLogger(SeasonCreateService.class);

    public static final String SCORING_TYPE_EMPTY = "Please provide a season scoring type.";

    private static final String UNEXPECTED_GAME_ERROR = "Sorry, we had an issue finding the selected game. " +
            "Please try again.";

    @Transactional(rollbackFor = Exception.class)
    public SeasonResponse createSeason(SeasonCreateRequest seasonCreateRequest) throws Exception {

            validateSeasonName(seasonCreateRequest.getSeasonName());
            validateSeasonDates(seasonCreateRequest.getSeasonEndDate(), false);
            validateGameId(seasonCreateRequest.getSeasonGameId());

            Season season = buildSeason(seasonCreateRequest);

            return new SeasonResponse(season);
    }

    Season buildSeason(SeasonCreateRequest seasonCreateRequest) throws Exception {

        Optional<Game> bggGame = gameRepository.findById(seasonCreateRequest.getSeasonGameId());

        if (bggGame.isEmpty()) {
            bggGame = Optional.ofNullable(gameCreationService.createGame(seasonCreateRequest));
        }

        Season season = new Season();
        season.setName(seasonCreateRequest.getSeasonName());

        if (bggGame.isPresent()) {
            season.setGame(bggGame.get());
        } else {
            throw new SeasonException(UNEXPECTED_GAME_ERROR);
        }

        season.setStartDate(new Timestamp(System.currentTimeMillis()));
        season.setEndDate(getTimestamp(seasonCreateRequest.getSeasonEndDate()));

        if (seasonCreateRequest.getSeasonScoringType() == null ||
                "".equals(seasonCreateRequest.getSeasonScoringType())) {
            LOG.info("season scoring type was not provided by {} when creating season.",  getUsername());
            throw new SeasonException(SCORING_TYPE_EMPTY);
        }

        season.setScoringType(seasonCreateRequest.getSeasonScoringType());

        season.setStatus(SeasonStatus.ACTIVE.toString());

        // TODO: make sure the anonymous user or whatever cannot create seasons
        season.setCreator(getExecutingUser());

        try {
            seasonRepository.save(season);
        } catch (Exception e) {
            if (e.getCause() instanceof  ConstraintViolationException)
                throw new SeasonException(SEASON_NAME_EXISTS_ERROR_TEXT);
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

    void assignUserPermissionsForSeason(Season season) {

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

}
