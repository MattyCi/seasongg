package com.seasongg.season.controllers;

import com.seasongg.common.CommonController;
import com.seasongg.common.SggService;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.season.models.SeasonResponse;
import com.seasongg.season.models.SeasonEditRequest;
import com.seasongg.season.services.SeasonCreateService;
import com.seasongg.season.services.SeasonEditService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeasonController extends CommonController {

    @Autowired
    SeasonCreateService seasonCreateService;

    @Autowired
    SeasonEditService seasonEditService;

    private final Logger LOG = LoggerFactory.getLogger(SeasonController.class);
    private static final String SEASON_API = "/season";

    @RequestMapping(value = SEASON_API + "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createSeason(@RequestBody SeasonCreateRequest seasonCreateRequest) {

        try {
            return ResponseEntity.ok(seasonCreateService.createSeason(seasonCreateRequest));
        } catch (SggService.SggServiceException e) {
            return new ResponseEntity<>(new SeasonResponse(SggService.APPLICATION_ERROR, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Unexpected error during season creation with request: {}.", seasonCreateRequest.toString(), e);
            return new ResponseEntity<>(new SeasonResponse(SggService.UNKNOWN_ERROR,
                    SggService.UNKNOWN_ERROR_TEXT), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = SEASON_API + "/edit", method = RequestMethod.PUT)
    @PreAuthorize("@sggAuthorizationResolver.hasAuthority('season:editseason:' + #seasonEditRequest.getSeasonId())")
    public ResponseEntity<?> editSeason(@RequestBody SeasonEditRequest seasonEditRequest) {

        try {
            return ResponseEntity.ok(seasonEditService.editSeason(seasonEditRequest));
        } catch (SggService.SggServiceException e) {
            return new ResponseEntity<>(new SeasonResponse(SggService.APPLICATION_ERROR, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Unexpected error during season edit with request object: {}.", seasonEditRequest.toString(), e);
            return new ResponseEntity<>(new SeasonResponse(SggService.UNKNOWN_ERROR,
                    SggService.UNKNOWN_ERROR_TEXT), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
