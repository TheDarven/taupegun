package fr.thedarven.scenario.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Objects;

public class LavaLimiter extends OptionBoolean {
	
	private static String CANNOT_PLACE_LAVA = "Vous ne pouvez pas placer de la lave proche d'un joueur.";
	
	public LavaLimiter(TaupeGun main, InventoryGUI parent) {
		super(main, "Lava Limiter", "Désactive le placement de lave proche des autres joueurs.", "MENU_CONFIGURATION_SCENARIO_LAVALIMITER", Material.LAVA_BUCKET, parent, false);
		updateLanguage(getLanguage());
	}


	@Override
	public void updateLanguage(String language) {
		CANNOT_PLACE_LAVA = LanguageBuilder.getContent(getTranslationName(), "cannotPlace", language, true);

		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "cannotPlace", CANNOT_PLACE_LAVA);
		
		return languageElement;
	}
	
	
	
	
	
	
	
	
	/**
	 * Désactive le placage de lave proche d'un joueur
	 * 
	 * @param e L'évènement de placage d'un sceau
	 */
	@EventHandler
	final public void onPlaceLava(PlayerBucketEmptyEvent e) {
		if (!this.value)
			return;

		if (e.getBucket() != Material.LAVA_BUCKET)
			return;

		Player player = e.getPlayer();

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Objects.equals(p, player) || p.getGameMode() != GameMode.SURVIVAL)
				continue;

			if (e.getBlockClicked().getLocation().distance(p.getLocation()) - 1 < 6) {
				e.setCancelled(true);
				player.sendMessage("§c" + CANNOT_PLACE_LAVA);
				return;
			}
		}
	}
}
