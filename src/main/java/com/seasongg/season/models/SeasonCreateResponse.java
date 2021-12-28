package com.seasongg.season.models;

import com.seasongg.common.RestResponse;

public class SeasonCreateResponse extends RestResponse {

    private Season season;

    public SeasonCreateResponse(Season season) {
        super();
        this.season = season;
    }

    public SeasonCreateResponse(int status, String errorMessage) {
        super(status, errorMessage);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

}
