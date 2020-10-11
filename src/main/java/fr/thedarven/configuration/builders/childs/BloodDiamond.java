package fr.thedarven.configuration.builders.childs;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.main.metier.NumericHelper;

public class BloodDiamond extends OptionNumeric {

	public BloodDiamond(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public BloodDiamond(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * Lorsqu'on case un minerais de diamant, on inflige des dégats à l'utilisateur
	 * 
	 * @param e L'évènement de bloc cassé
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if(e.isCancelled())
			return;

		if(e.getBlock().getType().equals(Material.DIAMOND_ORE) && this.value > 0 && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			if(e.getPlayer().getHealth() > this.value) {
				e.getPlayer().damage(this.value);
			}else {
				e.getPlayer().setHealth(0.5);
			}				
		}	
	}

}