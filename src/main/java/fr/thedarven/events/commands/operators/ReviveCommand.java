package fr.thedarven.events.commands.operators;

import java.util.HashMap;
import java.util.Map;

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
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.statsgame.RestPlayerDeath;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.texts.TextInterpreter;
import net.md_5.bungee.api.ChatColor;

public class ReviveCommand implements CommandExecutor{
	
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("revive") && args.length > 0){
				if(p.isOp() || p.hasPermission("taupegun.revive")){
					if(!UtilsClass.molesEnabled() && args.length >= 1){
						for(PlayerTaupe pc : PlayerTaupe.getDeathPlayerManager()) {
							if(args[0].equalsIgnoreCase(pc.getName()) && pc.getPlayer() != null && pc.isOnline() && !pc.isAlive()) {
								TeamCustom team = pc.getStartTeam();
								if(team != null && !team.isSpectator()) {
									team.joinTeam(pc.getUuid());
									
									for(String player : team.getTeam().getEntries()){
										if(!player.equals(pc.getName())) {
											if(Bukkit.getPlayer(player) != null) {
												Bukkit.getPlayer(pc.getUuid()).teleport(Bukkit.getPlayer(player));
												Bukkit.getPlayer(pc.getUuid()).setGameMode(GameMode.SURVIVAL);
												Bukkit.getPlayer(pc.getUuid()).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2 ));
												pc.setAlive(true);
												SqlRequest.updateTaupeMort(pc.getUuid().toString(), 0);
												
												for(RestPlayerDeath playerDeath: RestGame.getCurrentGame().getStats().getPlayerDeath()) {
													if(playerDeath.getVictim().toString().equalsIgnoreCase(pc.getUuid().toString()))
														playerDeath.setRevived(true);
												}
												
												for(Player pl : Bukkit.getOnlinePlayers())
													pl.playSound(pl.getLocation(), Sound.ENDERDRAGON_DEATH , 1, 1);
												
												Map<String, String> params = new HashMap<String, String>();
												params.put("playerName", pc.getName());
												String reviveMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("COMMAND", "revive", InventoryRegister.language.getSelectedLanguage(), true), params);
																					
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
			}
		}
		return true;
	}

}
