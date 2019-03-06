package fr.thedarven.configuration.temp;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.utils.api.Title;

public class DiamondLimit extends OptionNumeric {
	
	private HashMap<UUID,Integer> buffer = new HashMap<UUID,Integer>();

	public DiamondLimit(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pPosition, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	public DiamondLimit(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(e.getBlock().getType().equals(Material.DIAMOND_ORE) && this.value > 0 && p.getGameMode().equals(GameMode.SURVIVAL)) {
			if(buffer.containsKey(p.getUniqueId())) {
				if(buffer.get(p.getUniqueId()) < this.value) {
					buffer.replace(p.getUniqueId(), buffer.get(p.getUniqueId())+1);
					Title.sendActionBar(p, ChatColor.BLUE+"DiamondLimit : "+ChatColor.WHITE+buffer.get(p.getUniqueId())+"/"+getValue());
				}else {
					p.sendMessage("§cVous avez dépassé la limite de diamant !");
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
				}
			}else {
				buffer.put(p.getUniqueId(), 1);
			}
		}	
	}
	
}