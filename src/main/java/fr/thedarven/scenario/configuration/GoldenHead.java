package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.CustomInventory;
import fr.thedarven.scenario.builder.OptionNumeric;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.thedarven.scenario.utils.NumericHelper;

public class GoldenHead extends OptionNumeric {
	
	public GoldenHead(TaupeGun main, CustomInventory parent) {
		super(main, "Golden Head", "Nombre de coeurs régénérés par les Golden Head.", "MENU_CONFIGURATION_OTHER_GOLDENHEAD", Material.SKULL_ITEM,
				parent, 4, new NumericHelper(0, 8, 0, 1, 1, "❤", 2, true, 1));
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
