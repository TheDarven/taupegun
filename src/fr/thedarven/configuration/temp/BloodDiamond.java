package fr.thedarven.configuration.temp;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;

public class BloodDiamond extends OptionNumeric {

	public BloodDiamond(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pPosition, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	public BloodDiamond(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(e.getBlock().getType().equals(Material.DIAMOND_ORE) && this.value > 0 && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			if(e.getPlayer().getHealth() > this.value) {
				e.getPlayer().damage(this.value);
			}else {
				e.getPlayer().setHealth(0.5);
			}				
		}	
	}

}