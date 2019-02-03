package de.fpyttel.kronedyp.api.googlecharts.data;

public class Cell {
	public Object v; // value of cell
	public String f; // formatted value of cell
	public Cell(Object v, String f) {
		super();
		this.v = v;
		this.f = f;
	}
}