package com.seasongg.user.models;

import com.seasongg.common.RestResponse;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationResponse that = (AuthenticationResponse) o;
        return jwt.equals(that.jwt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwt);
    }

}
