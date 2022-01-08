package com.seasongg.user.services;

import com.seasongg.common.SggService;
import com.seasongg.config.security.util.JwtUtil;
import com.seasongg.user.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService extends SggService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private final Logger LOG = LoggerFactory.getLogger(UserLoginService.class);

    public AuthenticationResponse loginUser(AuthenticationRequest authenticationRequest) {

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);

        final String jwt = jwtUtil.generateToken(authentication.getName());

        return new AuthenticationResponse(jwt);

    }

}
