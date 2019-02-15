package fr.thedarven.events.commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Teams;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;
import fr.thedarven.main.constructors.PlayerTaupe;
import fr.thedarven.utils.MessagesClass;
import fr.thedarven.utils.SqlRequest;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("g")) {
				if(p.isOp()){
					String message = " ";
					for(int i=0; i<args.length; i++){
						message = message+args[i]+" ";
					}
					Bukkit.broadcastMessage(ChatColor.YELLOW+"[Info]"+ChatColor.GREEN+message);
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}	
			}else if(cmd.getName().equalsIgnoreCase("heal") && TaupeGun.etat.equals(EnumGame.GAME)){
				if(p.isOp()){
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
					}
					Bukkit.broadcastMessage("§6[LGUHC] §aVous venez d'être soigné.");
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("revive") && args.length > 0){
				if(p.isOp()){
					if(TaupeGun.timer < InventoryRegister.annoncetaupes.getValue()*60 && args.length >= 1){
						for(PlayerTaupe pc : PlayerTaupe.getDeathPlayerManager()) {
							if(args[0].equals(pc.getCustomName()) &&  Bukkit.getPlayer(pc.getUuid()) != null && pc.isOnline()) {
								Set<Team> teams = Teams.board.getTeams();
								for(Team team : teams){
									if(team.getName().equals(pc.getTeamName())) {
										Teams.leaveTeam("Spectateurs", pc.getCustomName());
										Teams.joinTeam(pc.getTeamName(), pc.getCustomName());
										for(String player : team.getEntries()){
											if(!player.equals(pc.getCustomName())) {
												if(Bukkit.getPlayer(player) != null) {
													Bukkit.getPlayer(pc.getUuid()).teleport(Bukkit.getPlayer(player));
													Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" "+Bukkit.getPlayer(args[0]).getName()+" a été réssuscité.");
													for(Player pl : Bukkit.getOnlinePlayers()) {
														pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_DEATH , 1, 1);
													}
													Bukkit.getPlayer(pc.getUuid()).setGameMode(GameMode.SURVIVAL);
													Bukkit.getPlayer(pc.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2 ));
													pc.setAlive(true);
													SqlRequest.updateTaupeMort(pc.getUuid().toString(), 0);
													return true;
												}
											}
										}
										pc.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 0, Bukkit.getWorld("world").getHighestBlockYAt(0, 0)+2, 0));
									}
								}
								return true;
							}
						}
					}else{
						p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Il est impossible de réanimer quelqu'un à ce stade de la partie.");
					}
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("rules") || cmd.getName().equalsIgnoreCase("scenarios")){
				if(p.isOp() && TaupeGun.etat.equals(EnumGame.LOBBY)) {
					p.openInventory(InventoryRegister.menu.getInventory());
				}else if(InventoryRegister.scenariosvisibles.getValue()) {
					p.openInventory(InventoryRegister.configuration.getInventory());
				}
			}else if(cmd.getName().equalsIgnoreCase("taupelist")) {
				if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60){
					if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isAlive()) {
						MessagesClass.TaupeListMessage(p, 1);
						
						if(InventoryRegister.nombretaupes.getValue() == 2) {
							MessagesClass.TaupeListMessage(p, 2);
						}
						
						if(InventoryRegister.supertaupes.getValue()) {
							MessagesClass.SuperTaupeListMessage(p, 1);
						}
					}
				}else{
					MessagesClass.CannotCommandTaupelistMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("timer") && args.length > 0) {
				if(p.isOp()){
					TaupeGun.timer = Integer.parseInt(args[1]);
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}
		}
		return false;
	}
}
