package com.seasongg.common;

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

}
