package com.seasongg.season.models;

import java.math.BigInteger;

public class SeasonEditRequest {

    private BigInteger seasonId;
    private String seasonName;
    private String seasonEndDate;

    public SeasonEditRequest() { }

    public SeasonEditRequest(BigInteger seasonId, String seasonName, String seasonEndDate) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.seasonEndDate = seasonEndDate;
    }

    public BigInteger getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(BigInteger seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getSeasonEndDate() {
        return seasonEndDate;
    }

    public void setSeasonEndDate(String seasonEndDate) {
        this.seasonEndDate = seasonEndDate;
    }

    @Override
    public String toString() {
        return "SeasonEditRequest{" +
                "seasonId=" + seasonId +
                ", seasonName='" + seasonName + '\'' +
                ", seasonEndDate='" + seasonEndDate + '\'' +
                '}';
    }

}
