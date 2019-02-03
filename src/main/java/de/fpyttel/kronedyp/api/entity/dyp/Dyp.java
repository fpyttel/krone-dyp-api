package de.fpyttel.kronedyp.api.entity.dyp;

import java.util.List;

public class Dyp {

	private Integer id;
	private String date;
	private List<DypResult> results;
	
	public Dyp() {
		super();
	}
	
	public Dyp(Integer id, String date) {
		super();
		this.id = id;
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<DypResult> getResults() {
		return results;
	}

	public void setResults(List<DypResult> results) {
		this.results = results;
	}
	
}
