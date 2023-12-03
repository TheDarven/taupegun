package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionNumeric;
import fr.thedarven.scenario.utils.NumericParams;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamond extends OptionNumeric {

	public BloodDiamond(TaupeGun main, ConfigurationInventory parent) {
		super(main, "Blood Diamond", "Les diamants infligent des dégâts lorsqu'ils sont minés.", "MENU_CONFIGURATION_SCENARIO_BLOODDIAMOND",
				Material.TNT, parent, new NumericParams(0, 4, 0, 1, 1, "❤", 2, true, 1));
	}
	
	/**
	 * Lorsqu'on case un minerais de diamant, on inflige des dégâts à l'utilisateur
	 * 
	 * @param e L'évènement de bloc cassé
	 */
	@EventHandler
	final public void onBlockBreak(BlockBreakEvent e){
		if (e.isCancelled()) {
			return;
		}

		Player player = e.getPlayer();
		if (e.getBlock().getType() != Material.DIAMOND_ORE || this.value <= 0 || player.getGameMode() != GameMode.SURVIVAL) {
			return;
		}

		if (player.getHealth() > this.value) {
			player.damage(this.value);
		} else {
			player.setHealth(0.5);
		}
	}

}