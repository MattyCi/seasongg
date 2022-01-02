package com.seasongg.season.models;

import com.seasongg.common.RestResponse;

public class SeasonResponse extends RestResponse {

    private Season season;

    public SeasonResponse(Season season) {
        super();
        this.season = season;
    }

    public SeasonResponse(int status, String errorMessage) {
        super(status, errorMessage);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

}
