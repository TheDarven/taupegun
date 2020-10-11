package fr.thedarven.configuration.builders.childs;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;

public class LavaLimiter extends OptionBoolean {
	
	private static String CANNOT_PLACE_LAVA = "Vous ne pouvez pas placer de la lave proche d'un joueur.";
	
	public LavaLimiter(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	public LavaLimiter(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	
	
	

	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		CANNOT_PLACE_LAVA = LanguageBuilder.getContent(getTranslationName(), "cannotPlace", language, true);

		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
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
	public void onPlaceLava(PlayerBucketEmptyEvent e) {
		if(e.getBucket().equals(Material.LAVA_BUCKET)) {
			if(this.value) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(!p.equals(e.getPlayer()) && p.getGameMode().equals(GameMode.SURVIVAL)) {
						if(e.getBlockClicked().getLocation().distance(p.getLocation())-1 < 6) {
							e.setCancelled(true);
							e.getPlayer().sendMessage("§c"+CANNOT_PLACE_LAVA);
						}
					}
				}
			}
		}
	}
}
