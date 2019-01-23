package de.fpyttel.kronedyp.backend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamList extends ArrayList<Team> {

	private List<Integer> ranks = new ArrayList<Integer>();
	
	@Override
	public boolean add(Team team) {
		ranks.add(team.getRank());
		return super.add(team);
	}

	public int getRank(int playerId){
		// sort list
		Collections.sort(this);
		
		// find team
		int position = 0;
		for(Team t : this){
			if( t.getPlayerA() == playerId || t.getPlayerB() == playerId ){
				break;
			}
			position++;
		}
		
		// calculate rank
		return ranks.get(position);
	}
	
}
