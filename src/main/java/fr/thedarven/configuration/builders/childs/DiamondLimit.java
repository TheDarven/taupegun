package fr.thedarven.configuration.builders.childs;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.OptionNumeric;
import fr.thedarven.main.metier.NumericHelper;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;

public class DiamondLimit extends OptionNumeric {
	
	private static String EXCEEDED_LIMIT = "Vous avez dépassé la limite de diamant.";
	
	private HashMap<UUID,Integer> buffer = new HashMap<UUID,Integer>();

	public DiamondLimit(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, infos);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	public DiamondLimit(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, NumericHelper infos) {
		super(pName, pDescription, pTranslationName, pItem, pParent, infos);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		EXCEEDED_LIMIT = LanguageBuilder.getContent(getTranslationName(), "exceededLimit", language, true);

		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "exceededLimit", EXCEEDED_LIMIT);
		
		return languageElement;
	}
	
	
	
	
	/**
	 * Lorsqu'un joueur casse un bloc, sa diamond limit s'incrémente
	 * On le bloque dans le ca soù il a dépasser sa limite
	 * 
	 * @param e L'évènement de bloc cassé
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e){
		if(e.isCancelled())
			return;

		Player p = e.getPlayer();
		if(e.getBlock().getType().equals(Material.DIAMOND_ORE) && this.value > 0 && p.getGameMode().equals(GameMode.SURVIVAL)) {
			if(buffer.containsKey(p.getUniqueId())) {
				if(buffer.get(p.getUniqueId()) < this.value) {
					buffer.replace(p.getUniqueId(), buffer.get(p.getUniqueId())+1);
					Title.sendActionBar(p, ChatColor.BLUE+"DiamondLimit : "+ChatColor.WHITE+buffer.get(p.getUniqueId())+"/"+getValue());
				}else {
					p.sendMessage("§c"+EXCEEDED_LIMIT);
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
				}
			}else {
				buffer.put(p.getUniqueId(), 1);
			}
		}
	}
	
}