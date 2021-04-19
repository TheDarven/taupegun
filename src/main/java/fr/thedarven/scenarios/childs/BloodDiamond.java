package fr.thedarven.scenarios.childs;

import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionNumeric;
import fr.thedarven.scenarios.helper.NumericHelper;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamond extends OptionNumeric {

	public BloodDiamond(InventoryGUI parent) {
		super("Blood Diamond", "Les diamants infliges des dégâts lorsqu'ils sont minés.", "MENU_CONFIGURATION_SCENARIO_BLOODDIAMOND",
				Material.TNT, parent, new NumericHelper(0, 4, 0, 1, 1, "❤", 2, true, 1));
	}
	
	/**
	 * Lorsqu'on case un minerais de diamant, on inflige des dégâts à l'utilisateur
	 * 
	 * @param e L'évènement de bloc cassé
	 */
	@EventHandler
	final public void onBlockBreak(BlockBreakEvent e){
		if (e.isCancelled())
			return;

		Player player = e.getPlayer();

		if (e.getBlock().getType() != Material.DIAMOND_ORE || this.value <= 0 || player.getGameMode() != GameMode.SURVIVAL)
			return;

		if (player.getHealth() > this.value) {
			player.damage(this.value);
		} else {
			player.setHealth(0.5);
		}
	}

}