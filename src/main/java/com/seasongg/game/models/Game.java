package com.seasongg.game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seasongg.season.models.Season;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="games")
@NamedQuery(name="Game.findAll", query="SELECT g FROM Game g")
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="GAME_ID")
	private int gameId;

	@Column(name="GAME_NAME")
	private String gameName;

	//bi-directional many-to-one association to Season
	@OneToMany(mappedBy="game")
	@JsonIgnore
	private List<Season> seasons;

	public Game() {
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return this.gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public List<Season> getSeasons() {
		return this.seasons;
	}

	public void setSeasons(List<Season> seasons) {
		this.seasons = seasons;
	}

	public Season addSeason(Season season) {
		getSeasons().add(season);
		season.setGame(this);

		return season;
	}

	public Season removeSeason(Season season) {
		getSeasons().remove(season);
		season.setGame(null);

		return season;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Game game = (Game) o;
		return gameId == game.gameId && Objects.equals(gameName, game.gameName) && Objects.equals(seasons, game.seasons);
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameId, gameName, seasons);
	}
}