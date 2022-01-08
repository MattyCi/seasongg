package com.seasongg.season.services;

import com.seasongg.season.models.Season;
import com.seasongg.season.models.SeasonResponse;
import com.seasongg.season.models.SeasonEditRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class SeasonEditService extends SeasonService {

    private final Logger LOG = LoggerFactory.getLogger(SeasonEditService.class);
    private static final String NO_EDITS = "No changes were provided, so the season was not edited.";

    @Transactional(rollbackFor = Exception.class)
    public SeasonResponse editSeason(SeasonEditRequest seasonEditRequest) throws Exception {

        validateSeasonId(seasonEditRequest.getSeasonId());

        Season seasonToEdit = findSeason(seasonEditRequest.getSeasonId());

        boolean editsOccurred = false;

        if (seasonEditRequest.getSeasonEndDate() != null && !seasonEditRequest.getSeasonEndDate().isEmpty()) {
            changeSeasonEndDate(seasonToEdit, seasonEditRequest.getSeasonEndDate());
            editsOccurred = true;
        }

        if (seasonEditRequest.getSeasonName() != null && !seasonEditRequest.getSeasonName().isEmpty()) {
            changeSeasonName(seasonEditRequest, seasonToEdit);
            editsOccurred = true;
        }

        if (editsOccurred) {
            return new SeasonResponse(seasonToEdit);
        } else {
            throw new SeasonException(NO_EDITS);
        }

    }

    private void changeSeasonEndDate(Season seasonToEdit, String endDate) throws SeasonException {

        validateSeasonDates(endDate, true);
        seasonToEdit.setEndDate(getTimestamp(endDate));
        adjustSeasonStatusIfNeeded(seasonToEdit);

    }

    private void adjustSeasonStatusIfNeeded(Season seasonToEdit) {

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (seasonToEdit.getEndDate().before(currentTime)) {
            seasonToEdit.setStatus("I");
        } else {
            seasonToEdit.setStatus("A");
        }

    }

    private void changeSeasonName(SeasonEditRequest seasonEditRequest, Season seasonToEdit) throws SeasonException {

        validateSeasonName(seasonEditRequest.getSeasonName());

        if (seasonRepository.findByNameIgnoreCase(seasonEditRequest.getSeasonName()) != null)
            throw new SeasonException(SEASON_NAME_EXISTS_ERROR_TEXT);

        seasonToEdit.setName(seasonEditRequest.getSeasonName());

    }

}
