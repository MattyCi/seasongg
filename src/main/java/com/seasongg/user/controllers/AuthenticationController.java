package com.seasongg.user.controllers;

import com.seasongg.common.CommonController;
import com.seasongg.common.SggService;
import com.seasongg.season.models.SeasonResponse;
import com.seasongg.user.models.AuthenticationRequest;
import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.user.services.UserLoginService;
import com.seasongg.user.services.UserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController extends CommonController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserRegisterService userRegisterService;

    private final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping(value = PUBLIC_API + "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {

        try {
            return ResponseEntity.ok(userLoginService.loginUser(authenticationRequest));
        } catch (BadCredentialsException e) {
            LOG.info("Failed login attempt from user {}", authenticationRequest.getUsername());
            throw e;
        }

    }

    @RequestMapping(value = PUBLIC_API + "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {

        try {
            return ResponseEntity.ok(userRegisterService.registerUser(registrationRequest));
        } catch (SggService.SggServiceException e) {
            return new ResponseEntity<>(new SeasonResponse(SggService.APPLICATION_ERROR, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOG.error("Unexpected error when user tried creating account with username: {}.",
                    registrationRequest.getUsername(), e);
            return new ResponseEntity<>(new SeasonResponse(SggService.UNKNOWN_ERROR,
                    SggService.UNKNOWN_ERROR_TEXT), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
