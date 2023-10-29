package fr.thedarven.stats.model.dto;

import java.util.ArrayList;
import java.util.List;

public class GameStatsDto {
	private List<PlayerKillDto> playerKill;
	private List<PlayerDeathDto> playerDeath;
	private List<PlayerStatsDto> playerStat;
	
	public GameStatsDto() {
		playerKill = new ArrayList<>();
		playerDeath = new ArrayList<>();
		playerStat = new ArrayList<>();
	}
	
	public List<PlayerDeathDto> getPlayerDeath(){
		return this.playerDeath;
	}
	
	public void addPlayerKill(PlayerKillDto playerKill) {
		this.playerKill.add(playerKill);
	}
	
	public void addPlayerDeath(PlayerDeathDto playerDeath) {
		this.playerDeath.add(playerDeath);
	}
	
	public void addPlayerStat(PlayerStatsDto playerStat) {
		this.playerStat.add(playerStat);
	}
}
