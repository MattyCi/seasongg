package com.seasongg.users.models;

import com.seasongg.common.RestResponse;

public class AuthenticationResponse extends RestResponse {

    private String jwt;

    public AuthenticationResponse(String jwt) {
        super();
        this.jwt = jwt;
    }

    public AuthenticationResponse(int status, String errorMessage) {
        super(status, errorMessage);
    }

    public String getJwt() {
        return jwt;
    }

}
