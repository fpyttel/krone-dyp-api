package de.fpyttel.kronedyp.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.fpyttel.kronedyp.api.entity.dyp.Dyp;
import de.fpyttel.kronedyp.api.entity.dyp.DypPlayer;
import de.fpyttel.kronedyp.api.entity.dyp.DypResult;
import de.fpyttel.kronedyp.api.entity.dyp.DypTeam;
import de.fpyttel.kronedyp.api.model.Team;
import de.fpyttel.kronedyp.api.model.TeamList;

@Component
public class DypBF {

	@Autowired
	private EntityManager entityManager;

	public Dyp getDyp(int id) {
		Query q = entityManager.createNativeQuery(
				"SELECT  t.platz, p.vorname, p.nachname, t.punkte, t.spielerid, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.turnierid = :dypId AND t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 ORDER BY t.platz, t.teamid");
		q.setParameter("dypId", id);

		List<Object[]> ret = q.getResultList();

		// calculate team ranks
		TeamList teams = new TeamList();
		int lastPlayer = -1;
		double lastPlayerElo = -1;
		for (Object[] row : ret) {
			int rank = (int) row[0];
			int playerId = (int) row[4];
			double elo = (double) row[5];

			if (lastPlayer < 0) {
				// new team
				lastPlayer = playerId;
				lastPlayerElo = elo;
			} else {
				// add team
				teams.add(new Team(lastPlayer, playerId, null, null, lastPlayerElo, elo, rank));
				// reset
				lastPlayer = -1;
				lastPlayerElo = 0;
			}
		}

		final Dyp dyp = new Dyp();
		dyp.setId((int) id);
		dyp.setDate(getDypDate(id));

		int count = 0;
		DypTeam team = null;
		DypResult result = null;
		List<DypResult> resultList = new ArrayList<>();
		for (Object[] row : ret) {
			count++;

			if (ret.size() % 2 != 0 && count == ret.size()) {
				break;
			}

			int rank = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			int points = (int) row[3];
			int playerId = (int) row[4];
			double elo = (double) row[5];
			int calcRank = teams.getRank(playerId);

			if (count % 2 != 0) {
				team = new DypTeam(new DypPlayer(playerId, firstName, lastName, elo), null);
				result = new DypResult(rank, team, calcRank, points);
				resultList.add(result);
			} else {
				team.setPlayer2(new DypPlayer(playerId, firstName, lastName, elo));
			}
		}

		dyp.setResults(resultList);
		
		return dyp;
	}
	
	public List<Dyp> getDypList(){
		Query q = entityManager.createNativeQuery("SELECT t.id, t.anmeldeschluss FROM turnierlist as t ORDER BY t.id DESC");
		List<Object[]> ret = q.getResultList();
		
		List<Dyp> dypList = new ArrayList<>();
		String date;
		int dypId;
		for(Object[] row : ret){
			dypId = (int)row[0];
			date = parseDate((String)row[1]);
			dypList.add(new Dyp(dypId, date));
		}
	
		return dypList;
	}
	
	private String getDypDate(long id){
		Query q = entityManager.createNativeQuery("SELECT t.anmeldeschluss, t.id FROM turnierlist as t WHERE t.id = :dypId LIMIT 1");
		q.setParameter("dypId", id);
		
		List<Object[]> ret = q.getResultList();
		
		String date = (String)ret.get(0)[0];
		int dypId = (int)ret.get(0)[1];
		
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		
		return date;
	}
	
	private String parseDate(String dypDate){
		String date = dypDate;
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		return date;
	}

}
