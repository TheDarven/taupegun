package fr.thedarven.configuration.temp;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class LavaLimiter extends OptionBoolean {
	
	public LavaLimiter(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pPosition, pValue);
	}
	
	public LavaLimiter(String pName, String pDescription, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pItem, pParent, pValue);
	}
	
	@EventHandler
	public void onPlaceLava(PlayerBucketEmptyEvent e) {
		if(e.getBucket().equals(Material.LAVA_BUCKET)) {
			if(this.value) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!p.equals(e.getPlayer()) && p.getGameMode().equals(GameMode.SURVIVAL)) {
						if(e.getBlockClicked().getLocation().distance(p.getLocation())-1 < 6) {
							e.setCancelled(true);
							e.getPlayer().sendMessage("§cVous ne pouvez pas placer de la lave proche d'un joueur !");
						}
					}
				}
			}
		}
	}
}
