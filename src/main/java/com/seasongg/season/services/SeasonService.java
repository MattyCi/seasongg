package com.seasongg.season.services;

import com.seasongg.common.SggService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SeasonService extends SggService {

    @Autowired
    SeasonRepository seasonRepository;

    private final Logger LOG = LoggerFactory.getLogger(SeasonService.class);

    private static final String SEASON_DATE_FORMAT = "MM/dd/yyyy";

    private static final String NO_SEASON_SELECTED = "No season was provided to be edited.";
    private static final String SEASON_NAME_TOO_SHORT_ERROR_TEXT = "Season names must be at least 4 characters long.";
    private static final String SEASON_NAME_TOO_LONG_ERROR_TEXT = "Season names cannot be more than 56 " +
            "characters long.";
    static final String SEASON_NAME_EXISTS_ERROR_TEXT = "Sorry, but the season name you provided " +
            "already exists.";

    private static final String INVALID_END_DATE_ERROR_TEXT = "The season end date provided was invalid, please "
            + "choose a valid date with the format mm/dd/yyyy and try again.";
    private static final String END_DATE_BEFORE_START_ERROR_TEXT = "Please choose a date in the future for your " +
            "season end date.";

    private static final String NO_GAME_SELECTED_ERROR_TEXT = "Please select a game to be played " +
            "throughout the season.";

    void validateSeasonId(BigInteger seasonId) throws SeasonException {

        if (seasonId == null || seasonId.compareTo(BigInteger.ZERO) < 0) {
            LOG.info("user {} trying to edit season but unable to find seasonId of {} in database.",
                    getUsername(), seasonId);
            throw new SeasonException(NO_SEASON_SELECTED);
        }

    }

    void validateSeasonName(String seasonName) throws SeasonException {

        if (seasonName == null || seasonName.isEmpty()) {
            throw new SeasonException(SEASON_NAME_TOO_SHORT_ERROR_TEXT);
        }

        seasonName = seasonName.trim();

        if (seasonName.length() < 4) {
            throw new SeasonException(SEASON_NAME_TOO_SHORT_ERROR_TEXT);
        } else if (seasonName.length() > 56) {
            throw new SeasonException(SEASON_NAME_TOO_LONG_ERROR_TEXT);
        }

    }

    void validateSeasonDates(String seasonEndDate, boolean isEditSeason) throws SeasonException {

        if (seasonEndDate == null || seasonEndDate.isEmpty()) {
            throw new SeasonException(INVALID_END_DATE_ERROR_TEXT);
        }

        Timestamp seasonEndTimestamp;
        Timestamp seasonStartTimestamp;

        try {
            seasonEndTimestamp = getTimestamp(seasonEndDate);
            seasonStartTimestamp = new Timestamp(System.currentTimeMillis());
        } catch (Exception e) {
            LOG.error("unexpected error validating season end date for user {} and end date {}.", getUsername(),
                    seasonEndDate, e);
            throw new SeasonException(INVALID_END_DATE_ERROR_TEXT);
        }

        if (seasonEndTimestamp.before(seasonStartTimestamp) && !isEditSeason) {

            throw new SeasonException(END_DATE_BEFORE_START_ERROR_TEXT);

        }

    }

    Timestamp getTimestamp(String seasonEndDate) throws SeasonException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SEASON_DATE_FORMAT);
        Date parsedDate;

        try {
            parsedDate = dateFormat.parse(seasonEndDate);
        } catch (ParseException e) {
            LOG.error("Unexpected parsing exception for season date of: {}", seasonEndDate, e);
            throw new SeasonException(INVALID_END_DATE_ERROR_TEXT);
        }

        return new Timestamp(parsedDate.getTime());
    }

    void validateGameId(Integer seasonGameId) throws SeasonException {

        if (seasonGameId == null) {

            LOG.info("no game selected when user {} tried creating season.", getUsername());

            throw new SeasonException(NO_GAME_SELECTED_ERROR_TEXT);
        }

    }

    public static class SeasonException extends SggServiceException {
        public SeasonException(String message) {
            super(message);
        }
    }

}
