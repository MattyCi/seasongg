package com.seasongg.season.models;

public class SeasonCreateRequest {

    private String seasonName;

    private Integer seasonGameId;
    private String seasonGameName;
    private String seasonEndDate;
    private String seasonScoringType;

    public SeasonCreateRequest() { }

    public SeasonCreateRequest(String seasonName, Integer seasonGameId, String seasonGameName, String seasonEndDate,
                               String seasonScoringType) {
        this.seasonName = seasonName;
        this.seasonGameId = seasonGameId;
        this.seasonGameName = seasonGameName;
        this.seasonEndDate = seasonEndDate;
        this.seasonScoringType = seasonScoringType;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public Integer getSeasonGameId() {
        return seasonGameId;
    }

    public void setSeasonGameId(Integer seasonGameId) {
        this.seasonGameId = seasonGameId;
    }

    public String getSeasonGameName() {
        return seasonGameName;
    }

    public void setSeasonGameName(String seasonGameName) {
        this.seasonGameName = seasonGameName;
    }

    public String getSeasonEndDate() {
        return seasonEndDate;
    }

    public void setSeasonEndDate(String seasonEndDate) {
        this.seasonEndDate = seasonEndDate;
    }

    public String getSeasonScoringType() {
        return seasonScoringType;
    }

    public void setSeasonScoringType(String seasonScoringType) {
        this.seasonScoringType = seasonScoringType;
    }

    @Override
    public String toString() {
        return "SeasonCreateRequest{" +
                "seasonName='" + seasonName + '\'' +
                ", seasonGameId=" + seasonGameId +
                ", seasonGameName='" + seasonGameName + '\'' +
                ", seasonEndDate='" + seasonEndDate + '\'' +
                ", seasonScoringType='" + seasonScoringType + '\'' +
                '}';
    }

}
