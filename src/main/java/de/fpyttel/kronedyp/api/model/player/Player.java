package de.fpyttel.kronedyp.api.model.player;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import de.fpyttel.kronedyp.api.dao.entity.PlayerBE;

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

	public static class Mapper {
		public static List<Player> map(final List<PlayerBE> allPlayer) {
			return allPlayer.stream().map(p ->  new Player((int)p.getId(), p.getVorname(), p.getNachname(), null))
					.collect(Collectors.toList());
		}
	}

}
