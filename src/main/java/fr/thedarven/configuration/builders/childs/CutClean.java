package fr.thedarven.configuration.builders.childs;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionBoolean;

public class CutClean extends OptionBoolean {

	public CutClean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
	}
	
	public CutClean(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
	}
	
	/**
	 * Lorsqu'un item spawn, on le spawn cuit
	 * 
	 * @param e L'évènement de spawn d'un item
	 */
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e){
		if(this.value){
			ItemStack is = e.getEntity().getItemStack();
		    if(is == null){
		    	return;
		    }
		    ItemStack replace = null;
		    switch(is.getType()){
		    	case IRON_ORE:
		    		{
			    		replace = new ItemStack(Material.IRON_INGOT);
			    		ExperienceOrb exp = (ExperienceOrb) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
						exp.setExperience(1);
			    	}
		    		break;
		    	case GOLD_ORE: 
		    		{
			    		replace = new ItemStack(Material.GOLD_INGOT);
			    		ExperienceOrb exp = (ExperienceOrb) e.getLocation().getWorld().spawnEntity(e.getLocation(), EntityType.EXPERIENCE_ORB);
						exp.setExperience(1);
		    		}
		    		break;
		    	case SAND:
	    		{
		    		replace = new ItemStack(Material.GLASS);
		    	}
	    		break;
		    	default:
		    		return;
		    }
		    if(replace != null){
		      e.getEntity().setItemStack(replace);
		    }
		}
	}
	
	/**
	 * Lorsqu'un mob meurt on drop la nourriture cuite
	 * 
	 * @param e L'évènement de mort d'un mob
	 */
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		if(this.value){
		    List<ItemStack> loots = e.getDrops();
		    for(int i = loots.size() - 1; i >= 0; i--){
		    	ItemStack is = (ItemStack)loots.get(i);
		    	if (is == null) {
		    		return;
		    	}
		    	switch (is.getType()){
		    		case RAW_BEEF: 
			    		loots.remove(i);
			    		loots.add(new ItemStack(Material.COOKED_BEEF));
		    			break;
		    		case PORK: 
				        loots.remove(i);
				        loots.add(new ItemStack(Material.GRILLED_PORK));
				        break;
		    		case RAW_CHICKEN: 
		    			loots.remove(i);
		    			loots.add(new ItemStack(Material.COOKED_CHICKEN));
		    			break;
		    		case MUTTON: 
				        loots.remove(i);
				        loots.add(new ItemStack(Material.COOKED_MUTTON));
				        break;
		    		case RABBIT: 
		    			loots.remove(i);
		    			loots.add(new ItemStack(Material.COOKED_RABBIT));
		    			break;
		    		default:
		    			return;
		    	}
		    }
		}
	}

}
