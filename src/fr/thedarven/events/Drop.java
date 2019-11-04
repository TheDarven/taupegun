package fr.thedarven.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.constructors.EnumGame;

public class Drop implements Listener {

	public Drop(TaupeGun pl) {
	}
	
	@EventHandler
	public void onItemDrop (PlayerDropItemEvent e) {
		if((TaupeGun.etat == EnumGame.LOBBY || TaupeGun.etat == EnumGame.WAIT) && e.getItemDrop().getItemStack().getType() == Material.BANNER && e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("§eChoix de l'équipe")) {
			e.setCancelled(true);
		}
	}
}
