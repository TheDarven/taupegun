package fr.thedarven.events;

import java.util.HashMap;
import java.util.Map;

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

import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.statsgame.RestGame;
import fr.thedarven.statsgame.RestPlayerDeath;
import fr.thedarven.statsgame.RestPlayerKill;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.UtilsClass;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class Death implements Listener {

	public Death() {}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Player killer = victim.getKiller();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("playerName", "§6"+victim.getName()+"§r");
		String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		e.setDeathMessage(deathAllMessage);
		killPlayer(PlayerTaupe.getPlayerManager(e.getEntity().getUniqueId()),false);
		if(killer != null){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(killer.getUniqueId());
			pcKiller.setKill(pcKiller.getKill()+1);
			SqlRequest.updateTaupeKill(killer);
		}
		
		if(EnumGameState.isCurrentState(EnumGameState.GAME)) {
			String lastEntityTypeName = victim.getLastDamageCause() != null && victim.getLastDamageCause().getEntityType() != null 
					? victim.getLastDamageCause().getEntityType().toString() 
					: null;
			RestGame.getCurrentGame().addPlayerDeath(new RestPlayerDeath(victim.getUniqueId(), 
					victim.getLastDamageCause().getCause().toString(), lastEntityTypeName));
			if(killer != null && killer.getGameMode().equals(GameMode.SURVIVAL)){
				RestGame.getCurrentGame().addPlayerKill(new RestPlayerKill(victim.getUniqueId(), killer.getUniqueId(), killer.getHealth()));
			}
		}
	}
	
	public static void killPlayer(PlayerTaupe pl, boolean showMessage) {
		if(showMessage) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("playerName", "§6"+pl.getName()+"§r");
			String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", InventoryRegister.language.getSelectedLanguage(), true), params);
			
			Bukkit.broadcastMessage(deathAllMessage);
		}
		if(EnumGameState.isCurrentState(EnumGameState.GAME)){
			pl.setAlive(false);
			
			/* ON S'OCCUPE DU JOUEUR */
			TeamCustom team = pl.getTeam();
			for(Player playerOnline : Bukkit.getOnlinePlayers()) {
				playerOnline.playSound(playerOnline.getLocation(), Sound.WITHER_SPAWN, 1, 1);
			}
			
			if(UtilsClass.molesEnabled() && pl.isOnline()){
				Map<String, String> params = new HashMap<String, String>();
				params.put("playerName", pl.getName());
				String headName = "§6"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("ITEM", "head", InventoryRegister.language.getSelectedLanguage(), true), params);
				
				ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta teteM = (SkullMeta) tete.getItemMeta();
				teteM.setOwner(pl.getName());
				teteM.setDisplayName(headName);
				tete.setItemMeta(teteM);
				if(pl.getPlayer() != null)
					pl.getPlayer().getWorld().dropItem(pl.getPlayer().getLocation(), tete);
			}
			
			Player p = pl.getPlayer();
			if(p != null){
				p.getPlayer().setGameMode(GameMode.SPECTATOR);
				p.getPlayer().teleport(new Location(UtilsClass.getWorld(),0,200,0));
				p.getPlayer().sendMessage("§c"+LanguageBuilder.getContent("EVENT_DEATH", "deathMumble", InventoryRegister.language.getSelectedLanguage(), true));
				p.getPlayer().sendMessage("§c"+LanguageBuilder.getContent("EVENT_DEATH", "deathInfo", InventoryRegister.language.getSelectedLanguage(), true));
			}
			
			SqlRequest.updateTaupeMort(pl.getUuid().toString(), 1);
			if(team != null)
				team.leaveTeam(pl.getUuid());
			TeamCustom.getSpectatorTeam().joinTeam(pl.getUuid());
		}
	}
}
