package fr.thedarven.events;

import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.SqlRequest;

public class Death implements Listener {

	public Death(TaupeGun pl) {
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {			
		killPlayer(PlayerTaupe.getPlayerManager(e.getEntity().getUniqueId()));
		
		if(e.getEntity().getKiller() != null){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(e.getEntity().getKiller().getUniqueId());
			pcKiller.setKill(pcKiller.getKill()+1);;
			SqlRequest.updateTaupeKill(e.getEntity().getKiller());
		}
	}
	
	public static void killPlayer(PlayerTaupe pl) {
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			Bukkit.broadcastMessage(ChatColor.GOLD+""+pl.getCustomName()+ChatColor.RESET+" est mort");
			pl.setAlive(false);
			
			/* ON S'OCCUPE DU JOUEUR */
			Set<Team> teams = Teams.board.getTeams();
			for(Team team : teams){
				if(team.getName() != "Spectateurs"){
					for(String player : team.getEntries()){
						if(pl.getCustomName().equals(player)){
							for(Player playerOnline : Bukkit.getOnlinePlayers()) {
								playerOnline.playSound(playerOnline.getLocation(), Sound.WITHER_SPAWN, 1, 1);
							}
							
							if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60 && pl.isOnline()){
								ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
								SkullMeta teteM = (SkullMeta) tete.getItemMeta();
								teteM.setOwner(pl.getCustomName());
								teteM.setDisplayName(ChatColor.GOLD+"Tête de "+pl.getCustomName());
								tete.setItemMeta(teteM);
								
								pl.getPlayer().getWorld().dropItem(pl.getPlayer().getLocation(), tete);
								pl.getPlayer().setGameMode(GameMode.SPECTATOR);
								Location spawn = new Location(Bukkit.getWorld("world"),0,200,0);
								pl.getPlayer().teleport(spawn);
								pl.getPlayer().sendMessage("§cVous êtes à présent mort. Merci de vous muter ou de changer de channel mumble.");
								pl.getPlayer().sendMessage("§cVous pouvez savoir la liste des taupes en faisant /taupelist");
							}
							
							SqlRequest.updateTaupeMort(pl.getUuid().toString(), 1);
							Teams.leaveTeam(team.getName(),pl.getCustomName());		
							Teams.joinTeam("Spectateurs", pl.getCustomName());
						}
					}
				}			
			}
		}
	}
}
