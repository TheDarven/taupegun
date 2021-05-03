package fr.thedarven.scenarios.childs;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;

import java.util.Objects;
import java.util.Optional;

public class PotionII extends OptionBoolean{

	private static String TRANSFORM_POTION = "Poufff ! Votre potion est passée au niveau 1.";
	
	public PotionII(TaupeGun main, InventoryGUI parent) {
		super(main, "Potion II", "Active ou non les potions de niveau 2.", "MENU_CONFIGURATION_OTHER_POTION2", Material.POTION, parent,
				12, true, (byte) 8254);
		updateLanguage(getLanguage());
	}




	@Override
	public void updateLanguage(String language) {
		TRANSFORM_POTION = LanguageBuilder.getContent(getTranslationName(), "transformPotion", language, true);

		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "transformPotion", TRANSFORM_POTION);
		
		return languageElement;
	}


	/**
	 * Lorsqu'un joueur essaye d'upgrade des potions
	 *
	 * @param e L'évènement d'ajout d'un ingrédient à une potion
	 */
    @EventHandler
	final public void onPotionBrew(BrewEvent e) {
		if (this.value)
			return;

		if (e.getContents().getIngredient().getType() == Material.GLOWSTONE_DUST) {
			e.setCancelled(true);
		}
	}

	/**
	 * Lorsqu'une potion de niveau 2 est bu
	 *
	 * @param e L'évènement de consommation d'un item par un joueur
	 */
	@EventHandler
	final public void onPlayItemConsume(PlayerItemConsumeEvent e) {
		if (this.value)
			return;

		Optional<Potion> oPotion = getUpgradedPotion(e.getItem());
		if (!oPotion.isPresent())
			return;

		e.setCancelled(true);
		e.getPlayer().sendMessage("§a" + TRANSFORM_POTION);
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DIG_STONE, 10,1);

		Potion potion = oPotion.get();
		potion.setLevel(1);
		PlayerInventory inv = e.getPlayer().getInventory();
		inv.setItem(inv.getHeldItemSlot(), potion.toItemStack(e.getItem().getAmount()));
	}


	/**
	 * Renvoie l'objet Potion si l'ItemStack est une Potion de niveau au moins 2
	 *
	 * @param item L'item à convertir en Potion
	 * @return L'objet Potion si potion de niveau 2, rien sinon
	 */
	final public Optional<Potion> getUpgradedPotion(ItemStack item) {
		if (Objects.isNull(item) || item.getType() != Material.POTION)
			return Optional.empty();

		Potion potion = Potion.fromItemStack(item);
		if (potion.getLevel() <= 1) {
			return Optional.empty();
		}
		return Optional.of(potion);
	}
}
