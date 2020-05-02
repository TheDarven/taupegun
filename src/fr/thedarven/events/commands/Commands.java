package fr.thedarven.events.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.events.Death;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.texts.TextInterpreter;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("say")) {
				if(p.isOp()){
					String message = " ";
					for(int i=0; i<args.length; i++){
						message = message+args[i]+" ";
					}
					message = "§l︳ §e"+p.getName()+" §r§7≫§a"+message;
					Bukkit.broadcastMessage(" \n"+message+"\n ");
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("g")) {
				if(p.isOp()){
					String message = " ";
					for(int i=0; i<args.length; i++){
						message = message+args[i]+" ";
					}
					String infoMessage = LanguageBuilder.getContent("CONTENT", "info", InventoryRegister.language.getSelectedLanguage(), true);
					Bukkit.broadcastMessage("§e"+infoMessage+"§a"+message);
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}	
			}else if(cmd.getName().equalsIgnoreCase("heal") && EnumGameState.isCurrentState(EnumGameState.GAME)){
				if(p.isOp()){
					for(Player player : Bukkit.getOnlinePlayers()) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 100, true, false));
					}
					Bukkit.broadcastMessage("§6[TaupeGun] §a"+LanguageBuilder.getContent("COMMAND", "heal", InventoryRegister.language.getSelectedLanguage(), true));
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("playerkill") && args.length >= 1 && EnumGameState.isCurrentState(EnumGameState.GAME) && playerInGame(args[0]) != null){
				if(p.isOp()) {
					PlayerTaupe pl = PlayerTaupe.getPlayerManager(playerInGame(args[0]));
					if(!pl.isAlive()) {
						return false;
					}
					if(pl.isOnline()) {
						pl.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1000, 250));
					}else {
						Death.killPlayer(pl, true);
					}	
				}
				
			}else if(cmd.getName().equalsIgnoreCase("players")){
				String message = "§e"+LanguageBuilder.getContent("COMMAND", "playerList", InventoryRegister.language.getSelectedLanguage(), true);
				if(EnumGameState.isCurrentState(EnumGameState.LOBBY, EnumGameState.WAIT)) {
					for(PlayerTaupe player : PlayerTaupe.getAllPlayerManager()) {
						if(player.isOnline()) {
							message+="§a";
						}else {
							message+="§c";
						}
						message+=Bukkit.getOfflinePlayer(player.getUuid()).getName()+" §l§f︱ §r";
					}
				}else {
					for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
						if(player.isOnline()) {
							message+="§a";
						}else {
							message+="§c";
						}
						message+=Bukkit.getOfflinePlayer(player.getUuid()).getName()+" §l§f︱ §r";
					}	
				}
				p.sendMessage(message);
			}else if(cmd.getName().equalsIgnoreCase("revive") && args.length > 0){
				if(p.isOp()){
					if(TaupeGun.timer < InventoryRegister.annoncetaupes.getValue()*60 && args.length >= 1){
						for(PlayerTaupe pc : PlayerTaupe.getDeathPlayerManager()) {
							if(args[0].equalsIgnoreCase(pc.getCustomName()) && pc.getPlayer() != null && pc.isOnline() && !pc.isAlive()) {
								TeamCustom team = pc.getStartTeam();
								if(team != null) {
									team.joinTeam(pc.getUuid());
									
									for(String player : team.getTeam().getEntries()){
										if(!player.equals(pc.getCustomName())) {
											if(Bukkit.getPlayer(player) != null) {
												for(Player pl : Bukkit.getOnlinePlayers())
													pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_DEATH , 1, 1);
												Bukkit.getPlayer(pc.getUuid()).setGameMode(GameMode.SURVIVAL);
												Bukkit.getPlayer(pc.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2 ));
												pc.setAlive(true);
												SqlRequest.updateTaupeMort(pc.getUuid().toString(), 0);
												
												Map<String, String> params = new HashMap<String, String>();
												params.put("playerName", pc.getCustomName());
												String reviveMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "revive", InventoryRegister.language.getSelectedLanguage(), true), params);
												
												Bukkit.getPlayer(pc.getUuid()).teleport(Bukkit.getPlayer(player));
												
												Bukkit.broadcastMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.WHITE+" "+reviveMessage);
												return true;
											}
										}
									}
									World world = UtilsClass.getWorld();
									pc.getPlayer().teleport(new Location(world, 0, world.getHighestBlockYAt(0, 0)+2, 0));
								}
								return true;
							}
						}
					}else{
						p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" "+LanguageBuilder.getContent("COMMAND", "cannotRevive", InventoryRegister.language.getSelectedLanguage(), true));
					}
				}else {
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("rules") || cmd.getName().equalsIgnoreCase("scenarios")){
				if(p.isOp() && EnumGameState.isCurrentState(EnumGameState.LOBBY)) {
					p.openInventory(InventoryRegister.menu.getInventory());
				}else if(InventoryRegister.scenariosvisibles.getValue()) {
					p.openInventory(InventoryRegister.configuration.getInventory());
				}
			}else if(cmd.getName().equalsIgnoreCase("taupelist")) {
				if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60){
					if(!PlayerTaupe.getPlayerManager(p.getUniqueId()).isAlive()) {
						MessagesClass.TaupeListMessage(p);
						
						if(InventoryRegister.supertaupes.getValue() && InventoryRegister.annoncetaupes.getValue()*60+1200 == TaupeGun.timer)
							MessagesClass.SuperTaupeListMessage(p);
					}
				}else{
					p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" "+LanguageBuilder.getContent("COMMAND", "molesNotAnnounced", InventoryRegister.language.getSelectedLanguage(), true));
				}
			}else if(cmd.getName().equalsIgnoreCase("timer") && args.length > 0) {
				if(p.isOp()){
					try {
						TaupeGun.timer = Integer.parseInt(args[0]);	
					}catch(NumberFormatException e) {
						p.sendMessage(ChatColor.GREEN+"[TaupeGun]"+ChatColor.RED+" Nombre incorrect.");
					}
				}else{
					MessagesClass.CannotCommandOperatorMessage(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("updatestats") && args.length > 0) {
				if(args[0].equals("true")) {
					SqlRequest.updateGameDuree();	
				}	
			}
		}
		return false;
	}
	
	public UUID playerInGame(String name){
		if(name != null){
			for(PlayerTaupe player : PlayerTaupe.getAlivePlayerManager()) {
				if(Bukkit.getOfflinePlayer(player.getUuid()).getName().equals(name)) {
					return player.getUuid();
				}
			}
		}
		return null;
	}
}
