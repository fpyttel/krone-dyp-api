package de.fpyttel.kronedyp.api.entity.player;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = -7169804471932784141L;
	
	private Integer id;
	private String firstName;
	private String lastName;
	private PlayerStats stats;

	public Player() {
		super();
	}

	public Player(Integer id, String firstName, String lastName, PlayerStats stats) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.stats = stats;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public PlayerStats getStats() {
		return stats;
	}

	public void setStats(PlayerStats stats) {
		this.stats = stats;
	}

}
