package com.seasongg.user.models;

import com.seasongg.common.RestResponse;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegistrationResponse that = (RegistrationResponse) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username);
    }

}
