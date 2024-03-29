package de.fpyttel.kronedyp.api.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import de.fpyttel.kronedyp.api.model.dyp.Dyp;
import de.fpyttel.kronedyp.api.model.dyp.DypPlayer;
import de.fpyttel.kronedyp.api.model.dyp.DypResult;
import de.fpyttel.kronedyp.api.model.dyp.DypStatistic;
import de.fpyttel.kronedyp.api.model.dyp.DypTeam;
import de.fpyttel.kronedyp.api.model.team.Team;
import de.fpyttel.kronedyp.api.model.team.TeamList;

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

	public List<Dyp> getDypList() {
		Query q = entityManager
				.createNativeQuery("SELECT t.id, t.anmeldeschluss FROM turnierlist as t ORDER BY t.id DESC");
		List<Object[]> ret = q.getResultList();

		List<Dyp> dypList = new ArrayList<>();
		String date;
		int dypId;
		for (Object[] row : ret) {
			dypId = (int) row[0];
			date = parseDate((String) row[1]);
			dypList.add(new Dyp(dypId, date));
		}

		return dypList;
	}

	private String getDypDate(long id) {
		Query q = entityManager
				.createNativeQuery("SELECT t.anmeldeschluss, t.id FROM turnierlist as t WHERE t.id = :dypId LIMIT 1");
		q.setParameter("dypId", id);

		List<Object[]> ret = q.getResultList();

		String date = (String) ret.get(0)[0];
		int dypId = (int) ret.get(0)[1];

		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];

		return date;
	}

	public List<Object[]> getDypTeamElo(int id) {
		// fetch data
		Query q = entityManager.createNativeQuery(
				"SELECT  t.platz, p.vorname, p.nachname, t.punkte, t.spielerid, s.skill FROM playerlist as p, zzz_easy_tabelle t, uskillzlist as s WHERE t.turnierid = :dypId AND t.spielerid = p.id AND s.userid = p.id AND s.spielid = 11 ORDER BY t.platz, t.teamid");
		q.setParameter("dypId", id);
		List<Object[]> ret = q.getResultList();

		// calculate team ranks
		TeamList teams = new TeamList();
		int lastPlayer = -1;
		String lastPlayerName = null;
		double lastPlayerElo = -1;
		for (Object[] row : ret) {
			int rank = (int) row[0];
			String firstName = (String) row[1];
			String lastName = (String) row[2];
			int playerId = (int) row[4];
			double elo = (double) row[5];

			if (lastPlayer < 0) {
				// new team
				lastPlayer = playerId;
				lastPlayerElo = elo;
				lastPlayerName = lastName;
			} else {
				// add team
				teams.add(new Team(lastPlayer, playerId, lastPlayerName, lastName, lastPlayerElo, elo, rank));
				// reset
				lastPlayer = -1;
				lastPlayerElo = 0;
			}
		}

		Collections.sort(teams);

		// create model
		List<Object[]> teamDataList = new ArrayList<>();
		for (Team team : teams) {
			Object[] teamData = { team.getNameA() + " | " + team.getNameB(), team.getPlayerAElo(),
					team.getPlayerBElo() };
			teamDataList.add(teamData);
		}

		return teamDataList;
	}

	public DypStatistic getDypStatisitc(int dypId) {
		// get data
		final List<Object[]> teamEloData = getDypTeamElo(dypId);

		// calculate sums
		double sumDiff = 0;
		double sumElo = 0;
		List<Integer[]> teamElo = new ArrayList<>();
		List<Integer> playerElo = new ArrayList<>();
		for (Object[] data : teamEloData) {
			double player1Elo = (double) data[1];
			double player2Elo = (double) data[2];
			sumDiff += Math.abs(player1Elo - player2Elo);
			sumElo += player1Elo + player2Elo;
			teamElo.add(new Integer[] { (int) player1Elo, (int) player2Elo });
			playerElo.add((int) player1Elo);
			playerElo.add((int) player2Elo);
		}
		double avgEloDiff = sumDiff / teamEloData.size();
		double avgElo = sumElo / (teamEloData.size() * 2);

		// calculate median
		teamElo.sort(new Comparator<Integer[]>() {
			@Override
			public int compare(Integer[] o1, Integer[] o2) {
				return Integer.compare(o1[0] + o1[1], o2[0] + o2[1]);
			}
		});
		playerElo.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		int medianPlayerElo = playerElo.get(playerElo.size() / 2);
		int medianTeamIntex = teamElo.size() / 2;
		int medianTeamElo = teamElo.get(medianTeamIntex)[0] + teamElo.get(medianTeamIntex)[1];
		int teamsBalanced = teamElo.stream().filter(elos -> (elos[0] > avgElo && elos[1] <= medianPlayerElo)
				|| (elos[1] > avgElo && elos[0] <= medianPlayerElo)).collect(Collectors.toList()).size();
		int teamsUnbalanced = teamElo.size() - teamsBalanced;
		double balanceRatioRaw = (double) teamsBalanced / ((double) teamsBalanced + (double) teamsUnbalanced);
		double balanceRatio = Math.round(balanceRatioRaw * 100.0) / 100.0;

		return new DypStatistic((int) avgEloDiff, (int) avgElo, medianPlayerElo, medianTeamElo, teamsBalanced,
				teamsUnbalanced, balanceRatio);
	}

	private String parseDate(String dypDate) {
		String date = dypDate;
		date = date.split("-")[0];
		String[] tmp = date.split("/");
		date = tmp[2] + "." + tmp[1] + "." + tmp[0];
		return date;
	}

}
