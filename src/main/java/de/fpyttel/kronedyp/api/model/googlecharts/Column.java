package de.fpyttel.kronedyp.api.model.googlecharts;

public class Column {
	public String id; // id of column
	public String type; // type of column
    public String label; // label of column
	public Column(String id, String type, String label) {
		super();
		this.id = id;
		this.type = type;
		this.label = label;
	}
}