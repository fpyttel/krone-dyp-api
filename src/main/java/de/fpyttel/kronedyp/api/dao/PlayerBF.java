package de.fpyttel.kronedyp.api.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import de.fpyttel.kronedyp.api.dao.entity.PlayerBE;
import de.fpyttel.kronedyp.api.model.player.Player;
import de.fpyttel.kronedyp.api.model.player.PlayerStats;
import de.fpyttel.kronedyp.api.model.player.Teammate;

@Component
public class PlayerBF {

	@Autowired
	private EntityManager entityManager;

	public List<Player> getAllPlayer(){
		List<PlayerBE> allPlayer = entityManager.createNamedQuery("Player.getAll", PlayerBE.class).getResultList();
		return Player.Mapper.map(allPlayer);
	}
	
	@Cacheable("playerInfo")
	public Player getPlayer(int playerId) {
		// fetch base data
		Query q = entityManager.createNamedQuery("Player.getInfo");
		q.setParameter("playerId", playerId);
		Object[] row = (Object[]) (q.getResultList().get(0));

		// get base data
		String firstName = (String) row[0];
		String lastName = (String) row[1];
		double elo = (double) row[2];
		String lastDyp = (String) row[3];
		lastDyp = lastDyp.split("-")[0];
		String[] tmp = lastDyp.split("/");
		lastDyp = tmp[2] + "." + tmp[1] + "." + tmp[0];

		// fetch, get and calculate stats
		q = entityManager.createNamedQuery("Player.getDyps");
		q.setParameter("playerId", playerId);
		BigInteger dyps = (BigInteger) q.getResultList().get(0);

		q = entityManager.createNamedQuery("Player.getWins");
		q.setParameter("playerId", playerId);
		BigInteger wins = (BigInteger) q.getResultList().get(0);

		q = entityManager.createNamedQuery("Player.getMatches");
		q.setParameter("playerId", playerId);

		BigInteger matches = (BigInteger) q.getResultList().get(0);
		long loss = matches.longValue() - wins.longValue();
		double effectivity = wins.doubleValue() / (double) matches.longValue();

		// create model
		PlayerStats playerStats = new PlayerStats(elo, effectivity, lastDyp, dyps.intValue(), matches.intValue(),
				wins.intValue(), (int)loss);
		Player player = new Player(playerId, firstName, lastName, playerStats);

		return player;
	}
	
	@Cacheable("positions")
	public List<Object[]> getPositions(int playerId, Locale locale) {
		// fetch data
		Query q = entityManager.createNamedQuery("Player.getPositions");
		q.setParameter("playerId", playerId);
		List<Object[]> ret = q.getResultList();

		// create model
		List<Object[]> scoreList = new ArrayList<>();
		for (Object[] row : ret) {
			int position = (int) row[0];
			BigInteger count = (BigInteger) row[1];

			Object[] score = new Object[2];
			score[0] = position + ". " + getPositionString(locale);
			score[1] = count;
			scoreList.add(score);
		}

		return scoreList;
	}
	
	@Cacheable("eloHistory")
	public List<Object[]> getEloHistory(int playerId) {
		// fetch data
		Query q = entityManager.createNamedQuery("Player.getEloHistory");
		q.setParameter("playerId", playerId);
		List<String> ret = q.getResultList();

		// create model
		List<Object[]> eloList = new ArrayList<>();

		// calculate data
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
					Object[] eloData = {matchCounter++, elo};
					eloList.add(eloData);
					break;
				}
			}
		}

		return eloList;
	}
	
	@Cacheable("positionsHistory")
	public List<Object[]> getPositionsHistory(int playerId) {
		// fetch data
		Query q = entityManager.createNamedQuery("Player.getPositionsHistory");
		q.setParameter("playerId", playerId);
		List<Object[]> ret = q.getResultList();

		// create model
		List<Object[]> positionList = new ArrayList<>();
		int i = 0;
		for (Object[] dyp: ret) {
			int dypId = (int)dyp[0];
			int platz = (int)dyp[1];
			
			Object[] positionData = {dypId, platz};
			positionList.add(positionData);
		}

		return positionList;
	}
	
	@Cacheable("listAllPartner")
	public List<Teammate> getTeammates(int playerId){
		// fetch data
		Query qWin = entityManager.createNamedQuery("Player.getTeammateWins");
		qWin.setParameter("playerId", playerId);
		Query qDefeat = entityManager.createNamedQuery("Player.getTeammateLoss");
		qDefeat.setParameter("playerId", playerId);
		List<Object[]> retWin = qWin.getResultList();
		List<Object[]> retDefeat = qDefeat.getResultList();

		// create model
		List<Teammate> teammates = new ArrayList<>();

		// prepare data
		Map<Integer, Integer> mapDefeat = new HashMap<>();
		Map<Integer, String[]> mapName = new HashMap<>();
		for (Object[] row : retDefeat) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			BigInteger defeats = (BigInteger) row[3];
			mapDefeat.put(mateId, defeats.intValue());
			mapName.put(mateId, new String[]{firstName, lastName});
		}
		
		// add teammates with > 0 wins
		for (Object[] row : retWin) {
			String firstName = (String) row[0];
			String lastName = (String) row[1];
			int mateId = (int) row[2];
			int wins = ((BigInteger) row[3]).intValue();
			int loss = mapDefeat.get(mateId) != null ? mapDefeat.get(mateId) : 0;
			
			Teammate tm = new Teammate(mateId, firstName, lastName, wins, loss, (double)wins / (double)(loss + wins));
			teammates.add(tm);

			mapDefeat.remove(mateId);
		}
		
		// add 100% loser
		for(Integer mateId : mapDefeat.keySet()){
			Teammate tm = new Teammate(mateId, mapName.get(mateId)[0], mapName.get(mateId)[1], 0, mapDefeat.get(mateId), 0.0);
			teammates.add(tm);
		}
		
		// sort
		teammates.sort(new Comparator<Teammate>() {
			@Override
			public int compare(Teammate t1, Teammate t2) {
				return t1.getWins().compareTo(t2.getWins());
			}
		});
		Collections.reverse(teammates);

		return teammates;
	}
	
	private String getPositionString(Locale locale) {
		if(locale.getLanguage().equals(new Locale("de").getLanguage())) {
			return "Platz";
		} else {
			return "Position";
		}
	}
}
