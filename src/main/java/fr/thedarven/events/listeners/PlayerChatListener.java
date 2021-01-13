package fr.thedarven.events.listeners;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumGameState;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Objects;

public class PlayerChatListener implements Listener {

	private TaupeGun main;

	public PlayerChatListener(TaupeGun main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerChat(PlayerChatEvent e){
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(e.getPlayer().getUniqueId());
		Player player = e.getPlayer();
		if (EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
			e.setCancelled(true);
			Bukkit.broadcastMessage("§7" + getTeamColor(pl) + player.getName() + ": §r" + e.getMessage());
		} else if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			e.setCancelled(true);
			if (!pl.isAlive()) {
				String spectatorMessage = "§7" + LanguageBuilder.getContent("EVENT_TCHAT", "spectatorMessage", true) + player.getName() + ": " + e.getMessage();
				for (PlayerTaupe playerTaupe : PlayerTaupe.getAllPlayerManager()) {
					if (!playerTaupe.isAlive() && playerTaupe.isOnline()) {
						playerTaupe.getPlayer().sendMessage(spectatorMessage);
					}
				}
				return;
			}

			if (this.main.getInventoryRegister().tchatequipe.getValue()) {
				if (e.getMessage().startsWith("!") || e.getMessage().startsWith("*"))  {
					Bukkit.broadcastMessage(getTeamColor(pl)+player.getName() + ": §7" + e.getMessage().substring(1));
				} else {
					if (pl.isTaupe() && pl.getTeam() == pl.getTaupeTeam()) {
						MessagesClass.CommandTaupeMessageMessage(player, e.getMessage().split(" "), pl.getTeam());
					} else if (pl.isSuperTaupe() && pl.getTaupeTeam() == pl.getSuperTaupeTeam()) {
						MessagesClass.CommandSupertaupeMessageMessage(player, e.getMessage().split(" "), pl.getTeam());
					} else {
						String teamMessage = "§e" + LanguageBuilder.getContent("EVENT_TCHAT", "teamMessage", true)+"§7" + player.getName() + ": " + e.getMessage();
						pl.getTeam().getAlivesPlayers().forEach(playerTaupe -> playerTaupe.getPlayer().sendMessage(teamMessage));
					}
				}
			} else {
				Bukkit.broadcastMessage(getTeamColor(pl) + player.getName() + ": §7" + e.getMessage());
			}
		}
	}
	
	@EventHandler
	public void writeCommand(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(player.getUniqueId());
		if (EnumGameState.isCurrentState(EnumGameState.GAME)) {
			if (e.getMessage().startsWith("/me") && !pl.isAlive()){
				e.setCancelled(true);
			} else if ((e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/msg")) && !pl.isAlive()) {
				e.setCancelled(true);
				String cannotPrivateMessage = "§a[TaupeGun]§c "+LanguageBuilder.getContent("EVENT_TCHAT", "cannotPrivateMessage", true);
				player.sendMessage(cannotPrivateMessage);
			}
		}
	}
	
	private String getTeamColor(PlayerTaupe pl) {
		String color = "";
		if (!Objects.isNull(pl.getTeam()) && !Objects.isNull(pl.getTeam().getTeam())) {
			color = pl.getTeam().getTeam().getPrefix();
		}
		return color;
	}

}
