package de.fpyttel.kronedyp.api.model.player;

import java.io.Serializable;

public class PlayerStats implements Serializable {
	
	private static final long serialVersionUID = 6425920602186823037L;
	
	private Double elo;
	private Double effectivity;
	private String lastDypDate;
	private Integer dyps;
	private Integer matches;
	private Integer wins;
	private Integer loss;
	private Integer points;

	public PlayerStats() {
		super();
	}

	public PlayerStats(Double elo, Double effectivity, String lastDypDate, Integer dyps, Integer matches, Integer wins,
			Integer loss, Integer points) {
		super();
		this.elo = elo;
		this.effectivity = effectivity;
		this.lastDypDate = lastDypDate;
		this.dyps = dyps;
		this.matches = matches;
		this.wins = wins;
		this.loss = loss;
		this.points = points;
	}

	public Double getElo() {
		return elo;
	}

	public void setElo(Double elo) {
		this.elo = elo;
	}

	public Double getEffectivity() {
		return effectivity;
	}

	public void setEffectivity(Double effectivity) {
		this.effectivity = effectivity;
	}

	public String getLastDypDate() {
		return lastDypDate;
	}

	public void setLastDypDate(String lastDypDate) {
		this.lastDypDate = lastDypDate;
	}

	public Integer getDyps() {
		return dyps;
	}

	public void setDyps(Integer dyps) {
		this.dyps = dyps;
	}

	public Integer getMatches() {
		return matches;
	}

	public void setMatches(Integer matches) {
		this.matches = matches;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLoss() {
		return loss;
	}

	public void setLoss(Integer loss) {
		this.loss = loss;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

}
