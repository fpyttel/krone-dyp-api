package de.fpyttel.kronedyp.api.entity.dyp;

public class DypTeam {

	private DypPlayer player1;
	private DypPlayer player2;
	
	public DypTeam() {
		super();
	}

	public DypTeam(DypPlayer player1, DypPlayer player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
	}

	public DypPlayer getPlayer1() {
		return player1;
	}

	public void setPlayer1(DypPlayer player1) {
		this.player1 = player1;
	}

	public DypPlayer getPlayer2() {
		return player2;
	}

	public void setPlayer2(DypPlayer player2) {
		this.player2 = player2;
	}
	
}
