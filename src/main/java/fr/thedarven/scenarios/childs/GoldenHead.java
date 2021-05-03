package fr.thedarven.scenarios.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionNumeric;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.scenarios.helper.NumericHelper;

public class GoldenHead extends OptionNumeric {
	
	public GoldenHead(TaupeGun main, InventoryGUI parent) {
		super(main, "Golden Head", "Nombre de coeurs régénérés par les Golden Head.", "MENU_CONFIGURATION_OTHER_GOLDENHEAD", Material.SKULL_ITEM,
				parent, new NumericHelper(0, 8, 0, 1, 1, "❤", 2, true, 1));
	}
	
	/**
	 * Lorsqu'on mange une golden head
	 * 
	 * @param e L'évènement de mangeage
	 */
	@EventHandler
	final public void onEat(PlayerItemConsumeEvent e){
		if (this.value <= 0)
			return;

		Player player = e.getPlayer();
		if (e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals("§6Golden Head")){
			player.removePotionEffect(PotionEffectType.REGENERATION);
			player.removePotionEffect(PotionEffectType.ABSORPTION);
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * this.value, 1));
			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
		}
	}


}
