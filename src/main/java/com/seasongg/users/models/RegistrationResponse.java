package com.seasongg.users.models;

import com.seasongg.common.RestResponse;

public class RegistrationResponse extends RestResponse {

    private String username;

    public RegistrationResponse(String username) {
        super();
        this.username = username;
    }

    public RegistrationResponse(int status, String errorMessage) {
        super(status, errorMessage);
    }

    public String getUsername() {
        return username;
    }

}
