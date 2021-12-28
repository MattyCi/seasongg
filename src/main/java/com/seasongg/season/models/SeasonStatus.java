package com.seasongg.season.models;

public enum SeasonStatus {

	ACTIVE("A"), INACTIVE("I");

	private final String seasonStatus;

	SeasonStatus(final String seasonStatus) {
		this.seasonStatus = seasonStatus;
	}

	@Override
	public String toString() {
		return seasonStatus;
	}

}
