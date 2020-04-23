package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.NumericHelper;

public class Pvp extends OptionNumeric {
	
	public Pvp(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public Pvp(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * D�sactive les d�gats entre utilisateurs pendant la phase non PVP
	 * 
	 * @param e L'�v�nement de d�gat inflig�
	 */
	@EventHandler
    public void onOtherDamage(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
        	if(this.value*60 - TaupeGun.timer >= 0){
        		e.setCancelled(true);
        	}
        }
    }

}
