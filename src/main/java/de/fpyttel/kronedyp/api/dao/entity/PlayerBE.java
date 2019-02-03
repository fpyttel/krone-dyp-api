package de.fpyttel.kronedyp.api.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "playerlist")
public class PlayerBE {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String username;
	
	private String vorname;
	
	private String nachname;

	public PlayerBE(){}
	
	public PlayerBE(long id, String username, String vorname, String nachname) {
		super();
		this.id = id;
		this.username = username;
		this.vorname = vorname;
		this.nachname = nachname;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", username=" + username + ", vorname="
				+ vorname + ", nachname=" + nachname + "]";
	}
	
}
