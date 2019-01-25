package fr.thedarven.configuration.builders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import fr.thedarven.utils.CodeColor;

public class InventoryColor extends InventoryGUI {
	
	public InventoryColor() {
		super("Choix de la couleur", "", 2, Material.ACACIA_DOOR, null, 22);
		initItem();
	}
	
	private void initItem(){
		for(int color=0; color<14; color++){
    		ItemStack banner = new ItemStack(Material.BANNER, 1);
    		BannerMeta bannerM = (BannerMeta)banner.getItemMeta();
    		bannerM.setBaseColor(CodeColor.codeColorBD(color));
    		banner.setItemMeta(bannerM);
    				   
    		int colorRank = color;
    		if(color < 7){
    			colorRank = (color + 1);
    		}
    		if(color > 6){
    			colorRank = (color + 3);
    		}
    		getInventory().setItem(colorRank, banner);
		}
	}

}
