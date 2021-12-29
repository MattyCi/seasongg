package com.seasongg.user.controllers;

import com.seasongg.common.CommonController;
import com.seasongg.user.models.AuthenticationRequest;
import com.seasongg.user.models.AuthenticationResponse;
import com.seasongg.user.models.RegistrationRequest;
import com.seasongg.config.security.util.JwtUtil;
import com.seasongg.user.services.UserRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthenticationController extends CommonController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRegisterService userRegisterService;

    private final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping({ "/get-user-permissions" })
    public String firstPage(Authentication authentication) {
        return authentication.getAuthorities().toString();
    }

    @RequestMapping(value = PUBLIC_API + "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {

        try {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);

            final String jwt = jwtUtil.generateToken(authentication.getName());

            return ResponseEntity.ok(new AuthenticationResponse(jwt));

        } catch (BadCredentialsException e) {
            LOG.info("Failed login attempt from user {}", authenticationRequest.getUsername());
            throw e;
        }

    }

    @RequestMapping(value = PUBLIC_API + "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {

        return ResponseEntity.ok(userRegisterService.registerUser(registrationRequest));

    }


}
