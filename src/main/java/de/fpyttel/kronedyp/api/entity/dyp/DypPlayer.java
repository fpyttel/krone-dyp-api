package de.fpyttel.kronedyp.api.entity.dyp;

public class DypPlayer {

	private Integer id;
	private String firstName;
	private String lastName;
	private Double elo;

	public DypPlayer() {
		super();
	}

	public DypPlayer(Integer id, String firstName, String lastName, Double elo) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.elo = elo;
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

	public Double getElo() {
		return elo;
	}

	public void setElo(Double elo) {
		this.elo = elo;
	}

}
