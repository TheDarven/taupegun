package fr.thedarven.statsgame;

import java.util.ArrayList;
import java.util.List;

public class RestStats {
	private List<RestPlayerKill> playerKill;
	private List<RestPlayerDeath> playerDeath;
	private List<RestPlayerStat> playerStat;
	
	public RestStats() {
		playerKill = new ArrayList<>();
		playerDeath = new ArrayList<>();
		playerStat = new ArrayList<>();
	}
	
	public List<RestPlayerDeath> getPlayerDeath(){
		return this.playerDeath;
	}
	
	public void addPlayerKill(RestPlayerKill playerKill) {
		this.playerKill.add(playerKill);
	}
	
	public void addPlayerDeath(RestPlayerDeath playerDeath) {
		this.playerDeath.add(playerDeath);
	}
	
	public void addPlayerStat(RestPlayerStat playerStat) {
		this.playerStat.add(playerStat);
	}
}
