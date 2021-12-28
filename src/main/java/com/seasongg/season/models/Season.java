package com.seasongg.season.models;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;

import com.seasongg.game.models.Game;
import com.seasongg.round.models.Round;
import com.seasongg.user.models.Reguser;
import org.hibernate.annotations.OrderBy;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="season")
@NamedQuery(name="Season.findAll", query="SELECT s FROM Season s")
public class Season implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEASON_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private BigInteger seasonId;

	@Column(name="END_DATE")
	private Timestamp endDate;

	private String name;

	@Column(name="START_DATE")
	private Timestamp startDate;
	
	@Column(name="SCORING_TYPE")
	private String scoringType;

	//bidirectional many-to-one association to Round
	@OneToMany(mappedBy="season")
	@OrderBy(clause = "ROUND_DATE DESC")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<Round> rounds;

	//bidirectional many-to-one association to Game
	@ManyToOne
	@JoinColumn(name="GAME_ID")
	private Game game;

	//bi-directional many-to-one association to SeasonStanding
	@OneToMany(mappedBy="season")
	@OrderBy(clause = "PLACE asc, AVERAGED_POINTS desc")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<SeasonStanding> seasonStandings;
	
	//bi-directional many-to-one association to Reguser
	@ManyToOne
	@JoinColumn(name="CREATOR")
	private Reguser creator;
	
	@Column(name="STATUS", length=1)
	private String status;

	public Season() {
	}

	public BigInteger getSeasonId() {
		return this.seasonId;
	}

	public void setSeasonId(BigInteger seasonId) {
		this.seasonId = seasonId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public List<Round> getRounds() {
		return this.rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}

	public Round addRound(Round round) {
		getRounds().add(round);
		round.setSeason(this);

		return round;
	}

	public Round removeRound(Round round) {
		getRounds().remove(round);
		round.setSeason(null);

		return round;
	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<SeasonStanding> getSeasonStandings() {
		return this.seasonStandings;
	}

	public void setSeasonStandings(List<SeasonStanding> seasonStandings) {
		this.seasonStandings = seasonStandings;
	}

	public SeasonStanding addSeasonStanding(SeasonStanding seasonStanding) {
		getSeasonStandings().add(seasonStanding);
		seasonStanding.setSeason(this);

		return seasonStanding;
	}

	public SeasonStanding removeSeasonStanding(SeasonStanding seasonStanding) {
		getSeasonStandings().remove(seasonStanding);
		seasonStanding.setSeason(null);

		return seasonStanding;
	}
	
	public String getScoringType() {
		return this.scoringType;
	}

	public void setScoringType(String scoringType) {
		this.scoringType = scoringType;
	}
	
	public Reguser getCreator() {
		return this.creator;
	}

	public void setCreator(Reguser creator) {
		this.creator = creator;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}