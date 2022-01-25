package com.seasongg.common;

import java.util.Objects;

import static com.seasongg.common.SggService.SUCCESS;

public class RestResponse {

    private int status;
    private String errorMessage;

    public RestResponse() {
        this.status = SUCCESS;
        this.errorMessage = null;
    }

    public RestResponse(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestResponse that = (RestResponse) o;
        return status == that.status && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, errorMessage);
    }

}
