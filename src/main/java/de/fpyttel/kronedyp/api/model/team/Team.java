package de.fpyttel.kronedyp.api.model.team;

public class Team implements Comparable<Team>{

	private int playerA;
	private int playerB;
	
	private String nameA;
	private String nameB;

	private double playerAElo;
	private double playerBElo;
	
	private int rank;

	public Team(int playerA, int playerB, String nameA, String nameB, double playerAElo,
			double playerBElo, int rank) {
		super();
		this.playerA = playerA;
		this.playerB = playerB;
		this.nameA = nameA;
		this.nameB = nameB;
		this.playerAElo = playerAElo;
		this.playerBElo = playerBElo;
		this.rank = rank;
	}
	
	public Team(String nameA, String nameB){
		super();
		this.nameA = nameA;
		this.nameB = nameB;
	}

	public int getPlayerA() {
		return playerA;
	}

	public int getPlayerB() {
		return playerB;
	}

	public double getPlayerAElo() {
		return playerAElo;
	}

	public double getPlayerBElo() {
		return playerBElo;
	}
	
	public double getTeamElo(){
		return playerAElo + playerBElo;
	}

	public int getRank() {
		return rank;
	}
	
	public String getNameA() {
		return nameA;
	}

	public String getNameB() {
		return nameB;
	}

	@Override
	public int compareTo(Team team) {
		return Double.compare(team.getTeamElo(), getTeamElo());
	}

}
