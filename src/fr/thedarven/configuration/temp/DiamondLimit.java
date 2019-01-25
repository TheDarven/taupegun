package fr.thedarven.configuration.temp;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;

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
		if(e.getBlock().getType().equals(Material.DIAMOND_ORE) && this.value > 0 && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			if(buffer.containsKey(e.getPlayer().getUniqueId())) {
				if(buffer.get(e.getPlayer().getUniqueId()) < this.value) {
					buffer.replace(e.getPlayer().getUniqueId(), buffer.get(e.getPlayer().getUniqueId())+1);
				}else {
					e.getPlayer().sendMessage("§cVous avez dépassé la limite de diamant !");
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
				}
			}else {
				buffer.put(e.getPlayer().getUniqueId(), 1);
			}
		}	
	}
	
}