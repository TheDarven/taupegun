package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.ScenariosManager;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.helper.NumericHelper;

public class Pvp extends OptionNumeric {
	
	public Pvp(TaupeGun main, InventoryGUI parent) {
		super(main, "PvP", "La minute à laquelle le PvP s'active.", "MENU_CONFIGURATION_TIMER_PVP",Material.IRON_SWORD,
				parent, new NumericHelper(0, 30, 10, 1, 2, "min", 1, false, ScenariosManager.SECONDS_PER_MINUTE));
	}
	
	/**
	 * Désactive les dégâts entre utilisateurs pendant la phase non PVP
	 * 
	 * @param e L'évènement de dégat infligé
	 */
	@EventHandler
    final public void onOtherDamage(EntityDamageByEntityEvent e){
		if (this.isValueLower(this.main.getGameManager().getTimer()))
			return;

		if (e.getEntity() instanceof Player){
			if (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player) {
				e.setCancelled(true);
			}
		}
    }

}
