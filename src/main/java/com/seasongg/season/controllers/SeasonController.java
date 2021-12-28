package com.seasongg.season.controllers;

import com.seasongg.common.CommonController;
import com.seasongg.season.models.SeasonCreateRequest;
import com.seasongg.season.services.SeasonCreateService;
import com.seasongg.user.models.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeasonController extends CommonController {

    @Autowired
    SeasonCreateService seasonCreateService;

    private static final String SEASON_API = "/season";

    private final Logger LOG = LoggerFactory.getLogger(SeasonController.class);

    @RequestMapping(value = SEASON_API + "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createSeason(@RequestBody SeasonCreateRequest seasonCreateRequest) {

        return ResponseEntity.ok(seasonCreateService.createSeason(seasonCreateRequest));

    }

}
