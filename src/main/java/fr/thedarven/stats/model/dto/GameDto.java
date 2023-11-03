package fr.thedarven.stats.model.dto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import fr.thedarven.stats.model.enums.EnumRestStatType;
import fr.thedarven.utils.helpers.DateHelper;
import fr.thedarven.utils.helpers.RandomHelper;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import fr.thedarven.TaupeGun;
import fr.thedarven.player.model.PlayerTaupe;
import fr.thedarven.team.model.TeamCustom;

public class GameDto {

	final private static int ACCESS_KEY_LENGTH = 32;
	// final public static String REQUEST_ADDRESS = "http://darven.fr:8080/v1/taupegun";
	final public static String REQUEST_ADDRESS = "http://darven.fr:8081/api/v1/taupegun";

	private static GameDto currentGame = null;

	private TaupeGun main;

	private int duration;
	private long started_at;
	private String language;
	private String ip;

	private List<PlayerDto> players;

	private List<TeamDto> teams;

	private GameStatsDto stats;

	public GameDto(TaupeGun main) {
		if(currentGame != null)
			currentGame.endGame();

		this.main = main;

		this.started_at = DateHelper.getLongTimestamp();
		this.duration = 0;
		this.language = this.main.getLanguageManager().getLanguage();

		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.ip = "0.0.0.0";
		}

		players = new ArrayList<PlayerDto>();
		teams = new ArrayList<TeamDto>();
		stats = new GameStatsDto();

		currentGame = this;
	}

	public static GameDto getCurrentGame() {
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


	public List<PlayerDto> getPlayers() {
		return players;
	}


	public List<TeamDto> getTeams() {
		return teams;
	}


	public GameStatsDto getStats() {
		return stats;
	}

	public void setDuration(int duration) {
		if(currentGame != null)
			currentGame.duration = duration;
	}

	public void addPlayer(PlayerDto player) {
		if(currentGame != null)
			currentGame.players.add(player);
	}

	public void addTeam(TeamDto team) {
		if(currentGame != null)
			currentGame.teams.add(team);
	}

	public void addPlayerKill(PlayerKillDto playerKill) {
		if(currentGame != null)
			currentGame.stats.addPlayerKill(playerKill);
	}

	public void addPlayerDeath(PlayerDeathDto playerDeath) {
		if(currentGame != null)
			currentGame.stats.addPlayerDeath(playerDeath);
	}

	public void addPlayerStat(PlayerStatsDto playerStat) {
		if(currentGame != null)
			currentGame.stats.addPlayerStat(playerStat);
	}

	public void endGame() {
		if(currentGame == this) {
			if(Bukkit.getServer().getOnlineMode()) {
				this.duration = this.main.getGameManager().getTimer();

				for(PlayerTaupe pt: PlayerTaupe.getAllPlayerManager()) {
					TeamCustom team = pt.getStartTeam().orElse(TeamCustom.getSpectatorTeam());
					String teamName = (team == null) ? "" : team.getTeam().getName();

					addPlayer(new PlayerDto(pt.getUuid(), pt.getName(), pt.hasWin(), pt.getTaupeTeamNumber(), pt.getSuperTaupeTeamNumber(),
							teamName, pt.isSpectator(), pt.isAlive(), pt.getLastConnection(), pt.getUpdatedTimePlayed()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.MINED_DIAMOND, pt.getMinedDiamond()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.MINED_IRON, pt.getMinedIron()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.MINED_GOLD, pt.getMinedGold()));

					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.THROWED_ARROW, pt.getThrewArrow()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.USED_SWORD, pt.getUsedSword()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.BOW_FORCE_COUNTER, pt.getBowForceCounter()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.INFLICTED_ARROW_DAMAGE, pt.getInflictedArrowDamage()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.INFLICTED_SWORD_DAMAGE, pt.getInflictedSwordDamage()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.INFLICTED_DAMAGE, pt.getInflictedDamage()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.RECEIVED_ARROW_DAMAGE, pt.getReceivedArrowDamage()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.RECEIVED_SWORD_DAMAGE, pt.getReceivedSwordDamage()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.RECEIVED_DAMAGE, pt.getReceivedDamage()));

					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.HEALED_LIFE, pt.getHealedLife()));
					addPlayerStat(new PlayerStatsDto(pt.getUuid(), EnumRestStatType.ATE_GOLDEN_APPLE, pt.getAteGoldenApple()));
				}

				for(TeamCustom customTeam: TeamCustom.getAllStartTeams()) {
					Team team = customTeam.getTeam();
					addTeam(new TeamDto(team.getName(), team.getPrefix(), customTeam.isAlive(), customTeam.isSpectator()));
				}

				String[] caracters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ0123456789".split("");
				String accessToken = "";
				for(int i=0; i<ACCESS_KEY_LENGTH; i++)
					accessToken += caracters[RandomHelper.generate(caracters.length)];

				final String finalAccessToken = accessToken;
				final GameDto finalGameDto = this;

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
