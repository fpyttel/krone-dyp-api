package de.fpyttel.kronedyp.api.repo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fpyttel.kronedyp.api.googlecharts.data.Cell;
import de.fpyttel.kronedyp.api.googlecharts.data.Column;
import de.fpyttel.kronedyp.api.googlecharts.data.DataTable;
import de.fpyttel.kronedyp.api.googlecharts.data.Row;
import de.fpyttel.kronedyp.api.model.Team;
import de.fpyttel.kronedyp.api.model.TeamList;

@Repository
public class DypDao {

	@Autowired
	private EntityManager entityManager;

	private Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
			.create();

	@Cacheable("positions")
	public String positions(long playerId) {
		Query q = entityManager
				.createNativeQuery("SELECT platz, count(platz) FROM zzz_easy_tabelle WHERE spielerid = :playerId GROUP BY platz");
		q.setParameter("playerId", playerId);

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "string", "Platz"));
		data.cols.add(new Column("", "number", "Anzahl"));

		for (Object[] row : ret) {
			int platz = (int) row[0];
			BigInteger anz = (BigInteger) row[1];

			Row r = new Row();
			r.c.add(new Cell(platz + ". Platz", null));
			r.c.add(new Cell(anz, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}

	@Cacheable("positionsHistory")
	public String positionsHistory(long playerId) {
		Query q = entityManager
				.createNativeQuery("SELECT turnierid, platz FROM zzz_easy_tabelle WHERE spielerid = :playerId ORDER BY turnierid");
		q.setParameter("playerId", playerId);

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "DYP"));
		data.cols.add(new Column("", "number", "Platz"));

		int i = 0;
		for (Object[] dyp: ret) {
			int dypId = (int)dyp[0];
			int platz = (int)dyp[1];
			
			Row r = new Row();
			r.c.add(new Cell(dypId, null));
			r.c.add(new Cell(platz, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}

	@Cacheable("eloHistory")
	public String eloHistory(long playerId) {
		Query q = entityManager
				.createNativeQuery("SELECT skillzuwachs FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.turnierid = t.id AND t.turnierspielid = 11 ORDER BY r.id");
		q.setParameter("playerId", playerId);

		List<String> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "Match"));
		data.cols.add(new Column("", "number", "ELO"));

		String playerStr = playerId + "";

		double elo = 1500;
		long matchCounter = 0;
		for (String skillz : ret) {
			if (skillz == null || skillz.equals("")) {
				continue;
			}
			for (String t : skillz.split("~")) {
				String[] vals = t.split(":");
				if (vals[0].equals(playerStr)) {
					elo += Double.parseDouble(vals[1]);
					Row r = new Row();
					r.c.add(new Cell(matchCounter++, null));
					r.c.add(new Cell(elo, null));
					data.rows.add(r);
					break;
				}
			}
		}

		return gson.toJson(data);
	}

	public String listBestPartner(long playerId) {
		Query q = entityManager
				.createNativeQuery("SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as wincount FROM teammates as tm1, playerlist as p, (SELECT tm.teamid as winteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid = tm.teamid ORDER BY r.id) as win WHERE tm1.mateid != :playerId AND win.winteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY wincount DESC");
		q.setParameter("playerId", playerId);

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "playerId"));
		data.cols.add(new Column("", "string", "Mitspieler"));
		data.cols.add(new Column("", "number", "Siege"));
		// data.cols.add(new Column("", "number", "Niederlagen"));

		for (Object[] row : ret) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			BigInteger wins = (BigInteger) row[3];

			Row r = new Row();
			r.c.add(new Cell(mateId, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(wins, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}
	
	public String listWorstPartner(long playerId){
		Query q = entityManager
				.createNativeQuery("SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as defeatcount FROM teammates as tm1, playerlist as p, ( SELECT tm.teamid as defeatteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid != tm.teamid ORDER BY r.id) as defeat WHERE tm1.mateid != :playerId AND defeat.defeatteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY defeatcount DESC");
		q.setParameter("playerId", playerId);

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "playerId"));
		data.cols.add(new Column("", "string", "Mitspieler"));
		data.cols.add(new Column("", "number", "Niederlagen"));

		for (Object[] row : ret) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			BigInteger defeats = (BigInteger) row[3];

			Row r = new Row();
			r.c.add(new Cell(mateId, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(defeats, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}
	
	@Cacheable("listAllPartner")
	public String listAllPartner(long playerId){
		Query qWin = entityManager
				.createNativeQuery("SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as wincount FROM teammates as tm1, playerlist as p, (SELECT tm.teamid as winteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid = tm.teamid ORDER BY r.id) as win WHERE tm1.mateid != :playerId AND win.winteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY wincount DESC");
		qWin.setParameter("playerId", playerId);

		Query qDefeat = entityManager
				.createNativeQuery("SELECT p.vorname, p.nachname, tm1.mateid, COUNT(tm1.mateid) as defeatcount FROM teammates as tm1, playerlist as p, ( SELECT tm.teamid as defeatteam FROM resultlist as r, teammates as tm WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND winnerid != tm.teamid ORDER BY r.id) as defeat WHERE tm1.mateid != :playerId AND defeat.defeatteam = tm1.teamid AND p.id = tm1.mateid GROUP BY tm1.mateid ORDER BY defeatcount DESC");
		qDefeat.setParameter("playerId", playerId);
		
		List<Object[]> retWin = qWin.getResultList();
		List<Object[]> retDefeat = qDefeat.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "playerId"));
		data.cols.add(new Column("", "string", "Mitspieler"));
		data.cols.add(new Column("", "number", "Siege"));
		data.cols.add(new Column("", "number", "Niederlagen"));

		// prepare data
		Map<Integer, Integer> mapDefeat = new HashMap<Integer, Integer>();
		Map<Integer, String> mapName = new HashMap<Integer, String>();
		for (Object[] row : retDefeat) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			BigInteger defeats = (BigInteger) row[3];
			mapDefeat.put(mateId, defeats.intValue());
			mapName.put(mateId, firstName + " " + lastName);
		}
		
		for (Object[] row : retWin) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			BigInteger wins = (BigInteger) row[3];

			Row r = new Row();
			r.c.add(new Cell(mateId, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(wins, null));
			r.c.add(new Cell(mapDefeat.get(mateId) != null ? mapDefeat.get(mateId) : 0, null));
			data.rows.add(r);
			
			mapDefeat.remove(mateId);
		}
		
		for(Integer mateId : mapDefeat.keySet()){
			Row r = new Row();
			r.c.add(new Cell(mateId, null));
			r.c.add(new Cell(mapName.get(mateId), null));
			r.c.add(new Cell(0, null));
			r.c.add(new Cell(mapDefeat.get(mateId), null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}
	
	public String dypInfo(long id){
		Query q = entityManager.createNativeQuery("SELECT  t.platz, p.vorname, p.nachname, t.punkte, t.spielerid, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.turnierid = :dypId AND t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 ORDER BY t.platz, t.teamid");
		q.setParameter("dypId", id);
		
		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "Platz"));
		data.cols.add(new Column("", "string", "Name"));
		data.cols.add(new Column("", "number", "Punkte"));
		data.cols.add(new Column("", "number", "playerId"));
		data.cols.add(new Column("", "number", "elo"));
		data.cols.add(new Column("", "number", "calcRank"));

		// calculate team ranks
		TeamList teams = new TeamList();
		int lastPlayer = -1;
		double lastPlayerElo = -1;
		for (Object[] row : ret){
			int rank = (int) row[0];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			
			if( lastPlayer < 0 ){
				// new team 
				lastPlayer = playerId;
				lastPlayerElo = elo;
			}
			else {
				// add team
				teams.add(new Team(lastPlayer, playerId, null, null, lastPlayerElo, elo, rank));
				// reset
				lastPlayer = -1;
				lastPlayerElo = 0;
			}
		}
		
		int count = 0;
		for (Object[] row : ret) {
			count++;
			
			if( ret.size() % 2 != 0 && count == ret.size()) {
				break;
			}
			
			int rank = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			int points = (int) row[3];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			int calcRank = teams.getRank(playerId);

			Row r = new Row();
			r.c.add(new Cell(rank, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(points, null));
			r.c.add(new Cell(playerId, null));
			r.c.add(new Cell(elo, null));
			r.c.add(new Cell(calcRank, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}
	
	public String dypList(){
		Query q = entityManager.createNativeQuery("SELECT t.id, t.anmeldeschluss FROM turnierlist as t ORDER BY t.id DESC");
		
		List<Object[]> ret = q.getResultList();
		
		Map<Integer, String> info = new HashMap<>();

		String date;
		int dypId; 
		for(Object[] row : ret){
			dypId = (int)row[0];
			date = parseDate((String)row[1]);
			info.put(dypId, date);
		}
	
		return gson.toJson(info);
	}

	public String lastDypDate(){
		Query q = entityManager.createNativeQuery("SELECT t.anmeldeschluss, t.id FROM turnierlist as t WHERE t.id = (SELECT MAX(turnierid) FROM zzz_easy_tabelle) LIMIT 1");
		
		List<Object[]> ret = q.getResultList();
		
		String date = (String)ret.get(0)[0];
		int dypId = (int)ret.get(0)[1];
		
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("date", date);
		info.put("id", dypId);
	
		return gson.toJson(info);
	}
	
	public String dypDate(long id){
		Query q = entityManager.createNativeQuery("SELECT t.anmeldeschluss, t.id FROM turnierlist as t WHERE t.id = :dypId LIMIT 1");
		q.setParameter("dypId", id);
		
		List<Object[]> ret = q.getResultList();
		
		String date = (String)ret.get(0)[0];
		int dypId = (int)ret.get(0)[1];
		
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("date", date);
		info.put("id", dypId);
	
		return gson.toJson(info);
	}
	
	public String lastDyp() {
		Query q = entityManager
				.createNativeQuery("SELECT  t.platz, p.vorname, p.nachname, t.punkte, t.spielerid, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.turnierid = (SELECT MAX(turnierid) FROM zzz_easy_tabelle) AND t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 ORDER BY t.platz, t.teamid");

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "number", "Platz"));
		data.cols.add(new Column("", "string", "Name"));
		data.cols.add(new Column("", "number", "Punkte"));
		data.cols.add(new Column("", "number", "playerId"));
		data.cols.add(new Column("", "number", "elo"));
		data.cols.add(new Column("", "number", "calcRank"));

		// calculate team ranks
		TeamList teams = new TeamList();
		int lastPlayer = -1;
		double lastPlayerElo = -1;
		for (Object[] row : ret){
			int rank = (int) row[0];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			
			if( lastPlayer < 0 ){
				// new team 
				lastPlayer = playerId;
				lastPlayerElo = elo;
			}
			else{
				// add team
				teams.add(new Team(lastPlayer, playerId, null, null, lastPlayerElo, elo, rank));
				// reset
				lastPlayer = -1;
				lastPlayerElo = 0;
			}
		}
		
		for (Object[] row : ret) {
			int rank = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			int points = (int) row[3];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			int calcRank = teams.getRank(playerId);

			Row r = new Row();
			r.c.add(new Cell(rank, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(points, null));
			r.c.add(new Cell(playerId, null));
			r.c.add(new Cell(elo, null));
			r.c.add(new Cell(calcRank, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}

	public String scoreboard() {
		Query q = entityManager
				.createNativeQuery("SELECT p.id, p.vorname, p.nachname, SUM(t.punkte) as points, COUNT(t.turnierid) as dyps, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 GROUP BY p.id ORDER BY points DESC");

		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "string", "Name"));
		data.cols.add(new Column("", "number", "Punkte"));
		data.cols.add(new Column("", "number", "dyps"));
		data.cols.add(new Column("", "number", "elo"));

		for (Object[] row : ret) {
			int playerId = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			BigDecimal points = (BigDecimal) row[3];
			BigInteger dyps = (BigInteger) row[4];
			double elo = (double) row[5];

			Row r = new Row();
			r.c.add(new Cell(playerId, null));
			r.c.add(new Cell(firstName + " " + lastName, null));
			r.c.add(new Cell(points.longValue(), null));
			r.c.add(new Cell(dyps, null));
			r.c.add(new Cell(elo, null));
			data.rows.add(r);
		}

		return gson.toJson(data);
	}

	@Cacheable("playerInfo")
	public String playerInfo(long playerId) {
		Query q = entityManager
				.createNativeQuery("SELECT p.vorname, p.nachname, s.skill, (SELECT t.anmeldeschluss FROM playerlist as p, turnierlist as t, teammates as m, turnieranmeldung as a WHERE p.id = :playerId AND m.mateid = p.id AND t.id = a.turnierid AND a.teamid = m.teamid ORDER BY t.anmeldeschluss DESC LIMIT 1) as lastdyp FROM playerlist as p, uskillzlist as s WHERE p.id = :playerId AND s.userid = p.id LIMIT 1");
		q.setParameter("playerId", playerId);

		Object[] row = (Object[])(q.getResultList().get(0));

		String firstName = (String) row[0];
		String lastName = (String) row[1];
		double elo = (double) row[2];
		String lastDyp = (String) row[3];
		lastDyp = lastDyp.split("-")[0];
		String[] tmp = lastDyp.split("/");
		lastDyp = tmp[2] + "." + tmp[1] + "." + tmp[0];
		
		q = entityManager
				.createNativeQuery("SELECT COUNT(*) FROM playerlist as p, turnierlist as t, teammates as m, turnieranmeldung as a WHERE p.id = :playerId AND m.mateid = p.id AND t.id = a.turnierid AND a.teamid = m.teamid AND t.turnierspielid = 11 ORDER BY t.anmeldeschluss DESC LIMIT 1");
		q.setParameter("playerId", playerId);
		
		BigInteger dyps = (BigInteger) q.getResultList().get(0);
		
		q = entityManager
				.createNativeQuery("SELECT COUNT(*) FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.winnerid = tm.teamid AND r.turnierid = t.id AND t.turnierspielid = 11");
		q.setParameter("playerId", playerId);
		
		BigInteger wins = (BigInteger) q.getResultList().get(0);
		
		q = entityManager
				.createNativeQuery("SELECT COUNT(*) FROM resultlist as r, teammates as tm, turnierlist as t WHERE ((r.teamid0 = tm.teamid AND r.teamid1 != -1) OR (r.teamid1 = tm.teamid AND r.teamid0 != -1)) AND tm.mateid = :playerId AND r.winnerid != tm.teamid AND r.turnierid = t.id AND t.turnierspielid = 11");
		q.setParameter("playerId", playerId);
		
		BigInteger defeats = (BigInteger) q.getResultList().get(0);
		
		long matches = defeats.longValue() + wins.longValue();
		double effectivity = wins.doubleValue() / (double)matches;
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("name", firstName + " " + lastName);
		info.put("elo", elo);
		info.put("lastDyp", lastDyp);
		info.put("dyps", dyps);
		info.put("matches", matches);
		info.put("wins", wins.longValue());
		info.put("defeats", defeats.longValue());
		info.put("effectivity", effectivity);
		
		return gson.toJson(info);
	}
	
	public String dypTeams(long id){
		Query q = entityManager
				.createNativeQuery("SELECT  t.platz, p.vorname, p.nachname, t.punkte, t.spielerid, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.turnierid = :dypId AND t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 ORDER BY t.platz, t.teamid");
		q.setParameter("dypId", id);
		
		List<Object[]> ret = q.getResultList();

		DataTable data = new DataTable();
		data.cols.add(new Column("", "string", "Team"));
		data.cols.add(new Column("", "number", "ELO 1"));
		data.cols.add(new Column("", "number", "ELO 2"));

		// calculate team ranks
		TeamList teams = new TeamList();
		int lastPlayer = -1;
		String lastPlayerName = null;
		double lastPlayerElo = -1;
		for (Object[] row : ret){
			int rank = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			
			if( lastPlayer < 0 ){
				// new team 
				lastPlayer = playerId;
				lastPlayerElo = elo;
				lastPlayerName = lastName;
			}
			else {
				// add team
				teams.add(new Team(lastPlayer, playerId, lastPlayerName, lastName, lastPlayerElo, elo, rank));
				// reset
				lastPlayer = -1;
				lastPlayerElo = 0;
			}
		}
		
		Collections.sort(teams);
		
		for(Team team : teams){
			Row r = new Row();
			r.c.add(new Cell(team.getNameA() + " | " + team.getNameB(), null));
			r.c.add(new Cell(team.getPlayerAElo(), null));
			r.c.add(new Cell(team.getPlayerBElo(), null));
			data.rows.add(r);
		}
		
		return gson.toJson(data);
	}

	public String tree(long id) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		// get teams
		Map<Integer, Team> teams = getTeams(id);
		
		// get first round & create teams in JSON
		Query q = entityManager
				.createNativeQuery("SELECT r.teamid0, r.teamid1, r.winnerid FROM resultlist as r WHERE r.turnierid = :treeId AND r.rundenid = 0 ORDER BY r.matchid");
		q.setParameter("treeId", id);
		
		List<String[]> teamList = new ArrayList<String[]>();
		
		List<Object[]> retFirst = q.getResultList();
		for(Object[] row : retFirst){
			int teamId0 = (int)row[0];
			int teamId1 = (int)row[1];
			int winnerId = (int)row[2];
			Team team0 = teams.get(teamId0);
			Team team1 = teams.get(teamId1);
			if(team0 == null && team1 == null) {
				teamList.add(new String[]{null, null});
			}
			else if( team0 == null ){
				teamList.add(new String[]{null, team1.getNameA() + "<br/>" + team1.getNameB()});
			}
			else if( team1 == null ){
				teamList.add(new String[]{team0.getNameA() + "<br/>" + team0.getNameB(), null});
			}
			else {
				teamList.add(new String[]{team0.getNameA() + "<br/>" + team0.getNameB(), team1.getNameA() + "<br/>" + team1.getNameB()});
			}
		}
		data.put("teams", teamList.toArray());
		
		// get WINNER BRACKET
		Query qWinner = entityManager
				.createNativeQuery("SELECT r.teamid0, r.teamid1, r.winnerid, r.matchid, r.rundenid FROM resultlist as r WHERE r.turnierid = :treeId AND r.rundenid >= 0 ORDER BY r.rundenid, r.matchid");
		qWinner.setParameter("treeId", id);
		
		List results = new ArrayList();
		List winList = new ArrayList<Integer[][]>();
		
		List<Object[]> retWinner = qWinner.getResultList();
		int actRound = 0;
		List<Integer[]> matches = new ArrayList<Integer[]>();
		for(Object[] row : retWinner){
			int teamId0 = (int)row[0];
			int teamId1 = (int)row[1];
			int winnerId = (int)row[2];
			int rundenId = (int)row[4];
			Team team0 = teams.get(teamId0);
			Team team1 = teams.get(teamId1);
			if( actRound == rundenId ){
				matches.add(winnerId == teamId0 ? new Integer[]{1,0} : new Integer[]{0,1});
			}
			else{
				winList.add(matches.toArray());
				matches.clear();
				actRound = rundenId;
				matches.add(winnerId == teamId0 ? new Integer[]{1,0} : new Integer[]{0,1});
			}
		}
		// last match is the final -> save it for later
		Object finalMatch = matches.toArray();
		
		// save winner bracket
		results.add(winList);

		
		// get LOSER BRACKET
		Query qLoser = entityManager
				.createNativeQuery("SELECT r.teamid0, r.teamid1, r.winnerid, r.matchid, r.rundenid FROM resultlist as r WHERE r.turnierid = :treeId AND r.rundenid < 0 ORDER BY r.rundenid DESC, r.matchid");
		qLoser.setParameter("treeId", id);
		
		List losList = new ArrayList<Integer[][]>();
		
		List<Object[]> retLoser = qLoser.getResultList();
		actRound = -1;
		matches.clear();
		int countRound = 0;
		for(Object[] row : retLoser){
			int teamId0 = (int)row[0];
			int teamId1 = (int)row[1];
			int winnerId = (int)row[2];
			int rundenId = (int)row[4];
			Team team0 = teams.get(teamId0);
			Team team1 = teams.get(teamId1);
			if( actRound == rundenId ){
				if( countRound % 2 == 0 ){
					matches.add(winnerId == teamId0 ? new Integer[]{1,0} : new Integer[]{0,1});
				}
				else{
					matches.add(winnerId == teamId0 ? new Integer[]{0,1} : new Integer[]{1,0});
				}
			}
			else{
				losList.add(matches.toArray());
				matches.clear();
				actRound = rundenId;
				countRound++;
				if( countRound % 2 == 0 ){
					matches.add(winnerId == teamId0 ? new Integer[]{1,0} : new Integer[]{0,1});
				}
				else{
					matches.add(winnerId == teamId0 ? new Integer[]{0,1} : new Integer[]{1,0});
				}
			}
		}
		// don't forget the last match
		losList.add(matches.toArray());
		
		// save loser bracket
		results.add(losList);
		
		// save final
		results.add(new Object[]{finalMatch});
		
		
		data.put("results", results.toArray());
		
//		List<String[]> teams = new ArrayList<String[]>();
//		teams.add(new String[]{"Krollmann, G.<br/>Virag, L.", "Schwolow, L.<br/>Buettner, H."});
//		teams.add(new String[]{"Team 3",  "Team 4"});
//		data.put("teams", teams.toArray());
		
		
		// get results
		// SELECT r.* FROM resultlist as r WHERE r.turnierid = 618 ORDER BY r.rundenid, r.id;
		
		
		return gson.toJson(data);
	} 
	
	private Map<Integer, Team> getTeams(long treeId){
		Query q = entityManager
				.createNativeQuery("SELECT ta.teamid, p.vorname, p.nachname FROM turnieranmeldung as ta, teammates as tm, playerlist as p WHERE ta.turnierid = :treeId AND ta.teamid = tm.teamid AND p.id = tm.mateid ORDER BY ta.teamid");
		q.setParameter("treeId", treeId);
		
		List<Object[]> ret = q.getResultList();
		
		Map<Integer, Team> teams = new HashMap<Integer, Team>();
		
		boolean isLast = false;
		String name = null;
		int count = 0;
		for (Object[] row : ret){
			count++;
			
			int teamId = (int)row[0];
			String firstname = (String)row[1];
			String lastname = (String)row[2];
			
			if( isLast ){
				teams.put(teamId, new Team(name, firstname.charAt(0) + ". " + lastname));
				isLast = false;
			}
			else {
				name = firstname.charAt(0) + ". " + lastname;
				isLast = true;
			}
			
			// last (single) player
			if( ret.size() % 2 == 1 && ret.size() == count ){
				System.out.println("added single team " + teamId);
				teams.put(teamId, new Team("", firstname.charAt(0) + ". " + lastname));
			}
		}
		
		return teams;
	}
	
	private String parseDate(String dypDate){
		String date = dypDate;
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		return date;
	}

}
