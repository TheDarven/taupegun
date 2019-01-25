package fr.thedarven.events.commands;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.EnumGame;
import fr.thedarven.main.PlayerTaupe;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.MessagesEventClass;
import fr.thedarven.utils.SqlRequest;

public class OthersCommands implements Listener {

	public OthersCommands(TaupeGun pl) {
	}

	@EventHandler
	public void onCommandes(PlayerCommandPreprocessEvent e){
		String[] args = e.getMessage().split(" ");
		Player p = e.getPlayer();
		if(args[0].equalsIgnoreCase("/g")){
			e.setCancelled(true);
			if(p.isOp()){
				MessagesEventClass.CommandGeneralMessage(e);
			}else{
				MessagesClass.CannotCommandOperatorMessage(p);
			}	
		}else if(args[0].equals("/heal") && TaupeGun.etat.equals(EnumGame.GAME)){
			e.setCancelled(true);
			if(p.isOp()){
				for(Player player : Bukkit.getOnlinePlayers()) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
				}
				Bukkit.broadcastMessage("§6[LGUHC] §aVous venez d'être soigné.");
			}else {
				MessagesClass.CannotCommandOperatorMessage(p);
			}
		}else if(args[0].equalsIgnoreCase("/revive")){
			e.setCancelled(true);
			if(p.isOp()){
				if(TaupeGun.timer < InventoryRegister.annoncetaupes.getValue()*60 && args.length >= 2){
					for(PlayerTaupe pc : PlayerTaupe.getDeathPlayerManager()) {
						if(args[1].equals(pc.getCustomName()) && Bukkit.getPlayer(pc.getUuid()) != null) {
							Set<Team> teams = Teams.board.getTeams();
							for(Team team : teams){
								if(team.getName().equals(pc.getTeamName())) {
									Teams.leaveTeam("Spectateurs", pc.getCustomName());
									Teams.joinTeam(pc.getTeamName(), pc.getCustomName());
									for(String player : team.getEntries()){
										if(!player.equals(pc.getCustomName())) {
											if(Bukkit.getPlayer(player) != null) {
												Bukkit.getPlayer(pc.getUuid()).teleport(Bukkit.getPlayer(player));
												MessagesEventClass.CommandReviveMessage(e);
												for(Player pl : Bukkit.getOnlinePlayers()) {
													pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_DEATH , 1, 1);
												}
												Bukkit.getPlayer(pc.getUuid()).setGameMode(GameMode.SURVIVAL);
												Bukkit.getPlayer(pc.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2 ));
												pc.setAlive(true);
												SqlRequest.updateTaupeMort(pc.getUuid().toString(), 0);
												return;
											}
										}
									}
								}
							}
						}
					}
				}else{
					MessagesClass.CannotCommandReviveMessage(p);
				}
			}else {
				MessagesClass.CannotCommandOperatorMessage(p);
			}
		}else if(args[0].equals("/scenarios")){
			e.setCancelled(true);
			if(p.isOp() && TaupeGun.etat.equals(EnumGame.LOBBY)) {
				p.openInventory(InventoryRegister.menu.getInventory());
			}
		}else if(args[0].equals("/rules")){
			e.setCancelled(true);
			if(InventoryRegister.scenariosvisibles.getValue()) {
				p.openInventory(InventoryRegister.configuration.getInventory());
			}
		}else if(args[0].equalsIgnoreCase("/taupelist")){
			e.setCancelled(true);
			if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60){
				if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isAlive()) {
					MessagesClass.Taupe1ListMessage(p);
					
					if(InventoryRegister.nombretaupes.getValue() == 2) {
						MessagesClass.Taupe2ListMessage(p);
					}
					
					if(InventoryRegister.supertaupes.getValue()) {
						MessagesClass.SuperTaupeListMessage(p);
					}
				}
			}else{
				MessagesClass.CannotCommandTaupelistMessage(p);
			}
		}
	}
}
