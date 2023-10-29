package fr.thedarven.events.listener;

import fr.thedarven.TaupeGun;
import fr.thedarven.model.enums.EnumGameState;
import fr.thedarven.stats.model.StatsPlayer;
import fr.thedarven.player.model.StatsPlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class PlayerChatListener implements Listener {

	private final TaupeGun main;

	public PlayerChatListener(TaupeGun main) {
		this.main = main;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		StatsPlayerTaupe pl = StatsPlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());
		Player player = e.getPlayer();
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			e.setCancelled(true);
			Bukkit.broadcastMessage("§7" + getTeamColor(pl) + player.getName() + ": §r" + e.getMessage());
		} else if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			e.setCancelled(true);
			if (!pl.isAlive()) {
				String spectatorMessage = "§7" + LanguageBuilder.getContent("EVENT_TCHAT", "spectatorMessage", true) + player.getName() + ": " + e.getMessage();
				for (StatsPlayerTaupe playerTaupe : StatsPlayerTaupe.getAllPlayerManager()) {
					if (!playerTaupe.isAlive() && playerTaupe.isOnline()) {
						playerTaupe.getPlayer().sendMessage(spectatorMessage);
					}
				}
				return;
			}

			if (this.main.getScenariosManager().teamTchat.getValue()) {
				if (e.getMessage().startsWith("!") || e.getMessage().startsWith("*"))  {
					Bukkit.broadcastMessage(getTeamColor(pl)+player.getName() + ": §7" + e.getMessage().substring(1));
				} else {
					if (pl.isTaupe() && pl.getTeam() == pl.getTaupeTeam()) {
						this.main.getMessageManager().moleSendsMoleMessage(player, pl, e.getMessage().split(" "));
					} else if (pl.isSuperTaupe() && pl.getTaupeTeam() == pl.getSuperTaupeTeam()) {
						this.main.getMessageManager().superMoleSendsSuperMoleMessage(player, pl, e.getMessage().split(" "));
					} else {
						String teamMessage = "§e" + LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true)+"§7" + player.getName() + ": " + e.getMessage();
						if (Objects.nonNull(pl.getTeam())) {
							pl.getTeam().getAlivesPlayers()
									.stream()
									.filter(StatsPlayer::isOnline)
									.forEach(playerTaupe -> playerTaupe.getPlayer().sendMessage(teamMessage));
						}
					}
				}
			} else {
				Bukkit.broadcastMessage(getTeamColor(pl) + player.getName() + ": §7" + e.getMessage());
			}
		}
	}

	private String getTeamColor(StatsPlayerTaupe pl) {
		String color = "";
		if (Objects.nonNull(pl.getTeam()) && Objects.nonNull(pl.getTeam().getTeam())) {
			color = pl.getTeam().getTeam().getPrefix();
		}
		return color;
	}

}
