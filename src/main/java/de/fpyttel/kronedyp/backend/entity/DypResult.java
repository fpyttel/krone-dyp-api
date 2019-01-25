package de.fpyttel.kronedyp.backend.entity;

public class DypResult {

	private Integer position;
	private DypTeam team;
	private Integer forecast;
	private Integer points;
	
	public DypResult() {
		super();
	}
	
	public DypResult(Integer position, DypTeam team, Integer forecast, Integer points) {
		super();
		this.position = position;
		this.team = team;
		this.forecast = forecast;
		this.points = points;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public DypTeam getTeam() {
		return team;
	}

	public void setTeam(DypTeam team) {
		this.team = team;
	}

	public Integer getForecast() {
		return forecast;
	}

	public void setForecast(Integer forecast) {
		this.forecast = forecast;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
	
}
