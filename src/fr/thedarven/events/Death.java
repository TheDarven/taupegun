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
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.MessagesEventClass;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.TeamDelete;
import fr.thedarven.main.EnumGame;
import fr.thedarven.main.PlayerTaupe;

public class Death implements Listener {

	public Death(TaupeGun pl) {
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		if(TaupeGun.etat.equals(EnumGame.GAME)){
			MessagesEventClass.DeathMessage(e);
			PlayerTaupe.getPlayerManager(p.getUniqueId()).setAlive(false);
			
			if(e.getEntity().getKiller() != null){
				PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(e.getEntity().getKiller().getUniqueId());
				pcKiller.setKill(pcKiller.getKill()+1);;
				Login.boards.get(e.getEntity().getKiller()).setLine(9, "➌ Kills :§e "+pcKiller.getKill());
				SqlRequest.updateTaupeKill(e.getEntity().getKiller());
			}
			
			/* ON S'OCCUPE DU JOUEUR */
			Set<Team> teams = Teams.board.getTeams();
			for(Team team : teams){
				if(team.getName() != "Spectateurs"){
					for(String player : team.getEntries()){
						if(p.getName().equals(player)){
							for(Player playerOnline : Bukkit.getOnlinePlayers()) {
								playerOnline.playSound(playerOnline.getLocation(), Sound.WITHER_SPAWN, 1, 1);
							}
							
							if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60){
								ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
								SkullMeta teteM = (SkullMeta) tete.getItemMeta();
								teteM.setOwner(p.getName());
								teteM.setDisplayName(ChatColor.GOLD+"Tête de "+p.getName());
								tete.setItemMeta(teteM);
								
								p.getWorld().dropItem(p.getLocation(), tete);
							}
							
							SqlRequest.updateTaupeMort(p.getUniqueId().toString(), 1);
							
							Teams.leaveTeam(team.getName(),p.getName());		
							Teams.joinTeam("Spectateurs", p.getName());
							p.setGameMode(GameMode.SPECTATOR);
							Location spawn = new Location(Bukkit.getWorld("world"),0,200,0);
							p.teleport(spawn);
						}
					}
				}			
			}
			
			MessagesClass.DeathMessage(p);
			TeamDelete.start();
		}
	}
}
