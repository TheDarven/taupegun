package fr.thedarven.statsgame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.thedarven.utils.UtilsClass;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;

public class RestGame {
	
	final private static int ACCESS_KEY_LENGTH = 32;
	// final public static String REQUEST_ADDRESS = "http://darven.fr:8080/v1/taupegun";
	final public static String REQUEST_ADDRESS = "http://darven.fr:8081/api/v1/taupegun";
	
	private static RestGame currentGame = null;

	private TaupeGun main;

	private int duration;
	private long started_at;
	private String language;	
	private String ip;

	private List<RestPlayer> players;
	
	private List<RestTeam> teams;
	
	private RestStats stats;

	public RestGame(TaupeGun main) {
		if(currentGame != null)
			currentGame.endGame();

		this.main = main;
		
		this.started_at = UtilsClass.getLongTimestamp();
		this.duration = 0;
		this.language = this.main.getInventoryRegister().language.getSelectedLanguage();
		
		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.ip = "0.0.0.0";
		}
		
		players = new ArrayList<RestPlayer>();
		teams = new ArrayList<RestTeam>();
		stats = new RestStats();
		
		currentGame = this;
	}
	
	public static RestGame getCurrentGame() {
		return currentGame;
	}



	public int getDuration() {
		return duration;
	}


	public long getStarted_at() {
		return started_at;
	}


	public String getLanguage() {
		return language;
	}


	public String getIp() {
		return ip;
	}


	public List<RestPlayer> getPlayers() {
		return players;
	}


	public List<RestTeam> getTeams() {
		return teams;
	}


	public RestStats getStats() {
		return stats;
	}
	
	public void setDuration(int duration) {
		if(currentGame != null)
			currentGame.duration = duration;
	}
	
	public void addPlayer(RestPlayer player) {
		if(currentGame != null)
			currentGame.players.add(player);
	}
	
	public void addTeam(RestTeam team) {
		if(currentGame != null)
			currentGame.teams.add(team);
	}
	
	public void addPlayerKill(RestPlayerKill playerKill) {
		if(currentGame != null)
			currentGame.stats.addPlayerKill(playerKill);
	}
	
	public void addPlayerDeath(RestPlayerDeath playerDeath) {
		if(currentGame != null)
			currentGame.stats.addPlayerDeath(playerDeath);
	}
	
	public void addPlayerStat(RestPlayerStat playerStat) {
		if(currentGame != null)
			currentGame.stats.addPlayerStat(playerStat);
	}
	
	public void endGame() {
		if(currentGame == this) {
			if(Bukkit.getServer().getOnlineMode()) {
				this.duration = TaupeGun.getInstance().getGameManager().getTimer();
				
				for(PlayerTaupe pt: PlayerTaupe.getAllPlayerManager()) {	
					TeamCustom team = pt.getStartTeam();
					if(team == null)
						team = TeamCustom.getSpectatorTeam();
					String teamName = (team == null) ? "" : team.getTeam().getName();
					
					addPlayer(new RestPlayer(pt.getUuid(), pt.getName(), pt.hasWin(), pt.getTaupeTeamNumber(), pt.getSuperTaupeTeamNumber(), 
							teamName, pt.isSpectator(), pt.isAlive(), pt.getLastConnection(), pt.getUpdatedTimePlayed()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.MINED_DIAMOND, pt.getMinedDiamond()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.MINED_IRON, pt.getMinedIron()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.MINED_GOLD, pt.getMinedGold()));
					
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.THROWED_ARROW, pt.getThrowedArrow()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.USED_SWORD, pt.getUsedSword()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.BOW_FORCE_COUNTER, pt.getBowForceCounter()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.INFLICTED_ARROW_DAMAGE, pt.getInflictedArrowDamage()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.INFLICTED_SWORD_DAMAGE, pt.getInflictedSwordDamage()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.INFLICTED_DAMAGE, pt.getInflictedDamage()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.RECEIVED_ARROW_DAMAGE, pt.getReceivedArrowDamage()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.RECEIVED_SWORD_DAMAGE, pt.getReceivedSwordDamage()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.RECEIVED_DAMAGE, pt.getReceivedDamage()));
					
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.HEALED_LIFE, pt.getHealedLife()));
					addPlayerStat(new RestPlayerStat(pt.getUuid(), EnumRestStatType.ATE_GOLDEN_APPLE, pt.getAteGoldenApple()));
				}
				
				for(TeamCustom customTeam: TeamCustom.getAllStartTeams()) {
					Team team = customTeam.getTeam();		
					addTeam(new RestTeam(team.getName(), team.getPrefix(), customTeam.isAlive(), customTeam.isSpectator()));
				}
				
				String[] caracters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ0123456789".split("");
				String accessToken = "";
				Random random = new Random();
				for(int i=0; i<ACCESS_KEY_LENGTH; i++)
					accessToken += caracters[random.nextInt(caracters.length)];
				
				final String finalAccessToken = accessToken;
				final RestGame finalRestGame = this;

				/* Bukkit.getScheduler().runTaskAsynchronously(TaupeGun.getInstance(), new Runnable() {
		            @Override
		            public void run() {
		            	try {         		
							String gameLink = RestRequest.sendPostRequest(finalAccessToken, new Gson().toJson(finalRestGame));
							System.out.println("[TaupeGun-SUCCESS] Game's stats sent");
							
							Map<String, String> params = new HashMap<String, String>();
							params.put("link", "§9§n"+gameLink+"§r§a");
							String statsLinkMessage = "§e[TaupeGun]§a "+TextInterpreter.textInterpretation(LanguageBuilder.getContent("CONTENT", "statsLink", InventoryRegister.language.getSelectedLanguage(), true), params);
							Bukkit.broadcastMessage(statsLinkMessage);
						} catch (IOException e) {
							System.out.println("[TaupeGun-ERROR] Failed to send game's stats");
						}
		            }
		        }); */
			}
			currentGame = null;
		}
	}
	
	public static void endGames() {
		if(currentGame != null)
			currentGame.endGame();
	}
}
