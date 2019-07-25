package de.fpyttel.kronedyp.api.model.dyp;

public class DypStatistic {

	private int avgEloDiff;
	private int avgElo;
	private int medianPlayerElo;
	private int medianTeamElo;
	private int teamsBalanced;
	private int teamsUnbalanced;
	private double balanceRatio;

	public DypStatistic(int avgEloDiff, int avgElo, int medianPlayerElo, int medianTeamElo, int teamsBalanced,
			int teamsUnbalanced, double balanceRatio) {
		super();
		this.avgEloDiff = avgEloDiff;
		this.avgElo = avgElo;
		this.medianPlayerElo = medianPlayerElo;
		this.medianTeamElo = medianTeamElo;
		this.teamsBalanced = teamsBalanced;
		this.teamsUnbalanced = teamsUnbalanced;
		this.balanceRatio = balanceRatio;
	}

	public int getMedianPlayerElo() {
		return medianPlayerElo;
	}

	public void setMedianPlayerElo(int medianPlayerElo) {
		this.medianPlayerElo = medianPlayerElo;
	}

	public int getAvgEloDiff() {
		return avgEloDiff;
	}

	public void setAvgEloDiff(int avgEloDiff) {
		this.avgEloDiff = avgEloDiff;
	}

	public int getAvgElo() {
		return avgElo;
	}

	public void setAvgElo(int avgElo) {
		this.avgElo = avgElo;
	}

	public int getMedianTeamElo() {
		return medianTeamElo;
	}

	public void setMedianTeamElo(int medianTeamElo) {
		this.medianTeamElo = medianTeamElo;
	}

	public int getTeamsBalanced() {
		return teamsBalanced;
	}

	public void setTeamsBalanced(int teamsBalanced) {
		this.teamsBalanced = teamsBalanced;
	}

	public int getTeamsUnbalanced() {
		return teamsUnbalanced;
	}

	public void setTeamsUnbalanced(int teamsUnbalanced) {
		this.teamsUnbalanced = teamsUnbalanced;
	}

	public double getBalanceRatio() {
		return balanceRatio;
	}

	public void setBalanceRatio(double balanceRatio) {
		this.balanceRatio = balanceRatio;
	}

}
