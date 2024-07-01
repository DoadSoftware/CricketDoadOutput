package com.cricket.containers;

import java.util.List;

import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;

public class Stats 
{
	private int PlayerId;
	private Team team;
	private Player player;
	private Statistics stats;
	private Tournament tournament;
	
	private List<Statistics> statsList;
	
	public Stats(int playerId, Team team, Player player, Statistics stats, Tournament tournament) {
		super();
		PlayerId = playerId;
		this.team = team;
		this.player = player;
		this.stats = stats;
		this.tournament = tournament;
	}

	public Stats(int playerId, Team team, Player player, List<Statistics> statsList) {
		super();
		PlayerId = playerId;
		this.team = team;
		this.player = player;
		this.statsList = statsList;
	}

	public int getPlayerId() {
		return PlayerId;
	}
	public void setPlayerId(int playerId) {
		PlayerId = playerId;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Statistics getStats() {
		return stats;
	}
	public void setStats(Statistics stats) {
		this.stats = stats;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public List<Statistics> getStatsList() {
		return statsList;
	}

	public void setStatsList(List<Statistics> statsList) {
		this.statsList = statsList;
	}

}