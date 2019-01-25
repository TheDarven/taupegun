package fr.thedarven.configuration.temp;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.main.TaupeGun;

public class Pvp extends OptionNumeric {
	
	public Pvp(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pPosition, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pPosition, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	public Pvp(String pName, String pDescription, Material pItem, InventoryGUI pParent, int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur) {
		super(pName, pDescription, pItem, pParent, pMin, pMax, pValue, pPas, pMorePas, pAfterName, pDiviseur);
	}
	
	@EventHandler
    public void onOtherDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
        	if(this.value*20 - TaupeGun.timer >= 0){
        		e.setCancelled(true);
        	}
        }
    }

}
