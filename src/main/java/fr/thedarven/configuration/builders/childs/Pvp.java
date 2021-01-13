package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.TaupeGun;
import fr.thedarven.models.NumericHelper;

public class Pvp extends OptionNumeric {
	
	public Pvp(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
	}
	
	public Pvp(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
	}
	
	/**
	 * Désactive les dégats entre utilisateurs pendant la phase non PVP
	 * 
	 * @param e L'évènement de dégat infligé
	 */
	@EventHandler
    public void onOtherDamage(EntityDamageByEntityEvent e){
		if (this.value * 60 < TaupeGun.getInstance().getGameManager().getTimer())
			return;

		if (e.getEntity() instanceof Player){
			if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
				e.setCancelled(true);
			}
		}
    }

}
