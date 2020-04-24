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
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.EnumGameState;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.main.metier.TeamCustom;
import fr.thedarven.utils.SqlRequest;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class Death implements Listener {

	public Death(TaupeGun pl) {
	}

	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("playerName", "§6"+p.getName()+"§r");
		String deathAllMessage = TextInterpreter.textInterpretation(LanguageBuilder.getContent("EVENT_DEATH", "deathAll", InventoryRegister.language.getSelectedLanguage(), true), params);
		
		e.setDeathMessage(deathAllMessage);
		killPlayer(PlayerTaupe.getPlayerManager(e.getEntity().getUniqueId()),false);
		
		if(p.getKiller() != null){
			PlayerTaupe pcKiller = PlayerTaupe.getPlayerManager(p.getKiller().getUniqueId());
			pcKiller.setKill(pcKiller.getKill()+1);;
			SqlRequest.updateTaupeKill(p.getKiller());
		}
	}
	
	public static void killPlayer(PlayerTaupe pl, boolean showMessage) {
		if(showMessage) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("playerName", "§6"+pl.getCustomName()+"§r");
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
			
			if(TaupeGun.timer >= InventoryRegister.annoncetaupes.getValue()*60 && pl.isOnline()){
				Map<String, String> params = new HashMap<String, String>();
				params.put("playerName", pl.getCustomName());
				String headName = "§6"+TextInterpreter.textInterpretation(LanguageBuilder.getContent("ITEM", "head", InventoryRegister.language.getSelectedLanguage(), true), params);
				
				ItemStack tete = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				SkullMeta teteM = (SkullMeta) tete.getItemMeta();
				teteM.setOwner(pl.getCustomName());
				teteM.setDisplayName(headName);
				tete.setItemMeta(teteM);
				if(pl.getPlayer() != null)
					pl.getPlayer().getWorld().dropItem(pl.getPlayer().getLocation(), tete);
			}
			
			Player p = pl.getPlayer();
			if(p != null){
				p.getPlayer().setGameMode(GameMode.SPECTATOR);
				p.getPlayer().teleport(new Location(Bukkit.getWorld("world"),0,200,0));
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
