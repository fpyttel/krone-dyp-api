package de.fpyttel.kronedyp.api.model.player;

import java.io.Serializable;

public class DypTeammate implements Serializable {

	private static final long serialVersionUID = -8819183365480625219L;

	private Integer dypId;
	private String date;
	private Integer mateId;
	private String firstName;
	private String lastName;
	private Integer position;

	public DypTeammate(Integer dypId, String date, Integer mateId, String firstName, String lastName, Integer position) {
		super();
		this.dypId = dypId;
		this.date = date;
		this.mateId = mateId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.position = position;
	}

	public Integer getDypId() {
		return dypId;
	}

	public void setDypId(Integer dypId) {
		this.dypId = dypId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getMateId() {
		return mateId;
	}

	public void setMateId(Integer mateId) {
		this.mateId = mateId;
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
