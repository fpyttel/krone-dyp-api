package de.fpyttel.kronedyp.api.dao.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "playerlist")
@NamedNativeQueries({
		@NamedNativeQuery(name = "Player.getInfo", query = "SELECT p.vorname, p.nachname, s.skill, (SELECT t.anmeldeschluss FROM playerlist as p, turnierlist as t, teammates as m, turnieranmeldung as a WHERE p.id = :playerId AND m.mateid = p.id AND t.id = a.turnierid AND a.teamid = m.teamid ORDER BY t.anmeldeschluss DESC LIMIT 1) as lastdyp FROM playerlist as p, uskillzlist as s WHERE p.id = :playerId AND s.userid = p.id LIMIT 1"),
		@NamedNativeQuery(name = "Player.getDyps", query = "SELECT COUNT(*) FROM playerlist as p, turnierlist as t, teammates as m, turnieranmeldung as a WHERE p.id = :playerId AND m.mateid = p.id AND t.id = a.turnierid AND a.teamid = m.teamid AND t.turnierspielid = 11 ORDER BY t.anmeldeschluss DESC LIMIT 1"),
		@NamedNativeQuery(name = "Player.getWins", query = "SELECT COUNT(*) FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.winnerid = tm.teamid AND r.turnierid = t.id AND t.turnierspielid = 11"),
		@NamedNativeQuery(name = "Player.getLoss", query = "SELECT COUNT(*) FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.winnerid != tm.teamid AND r.turnierid = t.id AND t.turnierspielid = 11"),
		@NamedNativeQuery(name = "Player.getMatches", query = "SELECT COUNT(*) FROM resultlist AS r LEFT JOIN turnierlist AS t ON r.turnierid = t.id LEFT JOIN teammates AS tm ON r.teamid0 = tm.teamid OR r.teamid1 = tm.teamid WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND t.turnierspielid = 11"),
		@NamedNativeQuery(name = "Player.getPoints", query = "SELECT SUM(t.punkte) as points FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 AND p.id = :playerId GROUP BY p.id ORDER BY points DESC"),
		@NamedNativeQuery(name = "Player.getPositions", query = "SELECT platz, count(platz) FROM zzz_easy_tabelle WHERE spielerid = :playerId GROUP BY platz"),
		@NamedNativeQuery(name = "Player.getEloHistory", query = "SELECT skillzuwachs FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.turnierid = t.id AND t.turnierspielid = 11 ORDER BY r.id"),
		@NamedNativeQuery(name = "Player.getPositionsHistory", query = "SELECT turnierid, platz FROM zzz_easy_tabelle WHERE spielerid = :playerId ORDER BY turnierid"),
		@NamedNativeQuery(name = "Player.getTeammateWins", query = "SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as wincount FROM teammates as tm1, playerlist as p, (SELECT tm.teamid as winteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid = tm.teamid ORDER BY r.id) as win WHERE tm1.mateid != :playerId AND win.winteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY wincount DESC"),
		@NamedNativeQuery(name = "Player.getTeammateLoss", query = "SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as defeatcount FROM teammates as tm1, playerlist as p, ( SELECT tm.teamid as defeatteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid != tm.teamid ORDER BY r.id) as defeat WHERE tm1.mateid != :playerId AND defeat.defeatteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY defeatcount DESC"),
		@NamedNativeQuery(name = "Player.getScoreboard", query = "SELECT p.id, p.vorname, p.nachname, SUM(t.punkte) as points, COUNT(t.turnierid) as dyps, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 GROUP BY p.id ORDER BY points DESC"),
		@NamedNativeQuery(name = "Player.getScoreboardByYear", query = "SELECT p.id, p.vorname, p.nachname, SUM(t.punkte) as points, COUNT(t.turnierid) as dyps, s.skill\r\n" + 
				"FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s, turnierlist as tl\r\n" + 
				"WHERE\r\n" + 
				"	t.spielerid = p.id\r\n" + 
				"	AND s.userid = p.id\r\n" + 
				"	AND s.spielid = 11\r\n" + 
				"	AND t.turnierid = tl.id\r\n" + 
				"	AND tl.anmeldeschluss BETWEEN CONCAT(:year,'/01/01-00:00:00') AND CONCAT(:year,'2018/12/31-23:59:59')\r\n" + 
				"GROUP BY p.id\r\n" + 
				"ORDER BY points DESC")
})
@NamedQueries({
	@NamedQuery(name = "Player.getAll", query = "SELECT p FROM PlayerBE p")
})
public class PlayerBE {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String username;

	private String vorname;

	private String nachname;

	public PlayerBE() {
	}

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
		return "Player [id=" + id + ", username=" + username + ", vorname=" + vorname + ", nachname=" + nachname + "]";
	}

}
