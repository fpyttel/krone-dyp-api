package de.fpyttel.kronedyp.api.entity.player;

import java.io.Serializable;

public class Teammate implements Serializable {

	private static final long serialVersionUID = 4412209747618358244L;
	
	private Integer playerId;
	private String firstName;
	private String lastName;
	private Integer wins;
	private Integer loss;
	private Double rate;

	public Teammate() {
		super();
	}

	public Teammate(Integer playerId, String firstName, String lastName, Integer wins, Integer loss, Double rate) {
		super();
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.wins = wins;
		this.loss = loss;
		this.rate = rate;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

}
