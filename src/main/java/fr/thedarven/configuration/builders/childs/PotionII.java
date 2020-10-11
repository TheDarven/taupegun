package fr.thedarven.configuration.builders.childs;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.configuration.builders.OptionBoolean;
import fr.thedarven.utils.languages.LanguageBuilder;

public class PotionII extends OptionBoolean{

	private static String TRANSFORM_POTION = "Poufff ! Votre potion est passé au niveau 1.";
	
	public PotionII(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, boolean pValue, byte pData) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pPosition, pValue, pData);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	public PotionII(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, boolean pValue, byte pData) {
		super(pName, pDescription, pTranslationName, pItem, pParent, pValue, pData);
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		TRANSFORM_POTION = LanguageBuilder.getContent(getTranslationName(), "transformPotion", language, true);

		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "transformPotion", TRANSFORM_POTION);
		
		return languageElement;
	}
	
	
	
	
	
	/**
	 * Lorsqu'une potion de niveau 2 est bue
	 * 
	 * @param e L'évènement de click d'un utilisateur dans le jeu
	 */
	@EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if(value)
            return;
        ItemStack item = e.getItem();
        if(item != null && (item.getType() == Material.POTION)){
        	
        	Potion potion = Potion.fromItemStack(item);
        	
	        if(potion.getLevel() > 1) {
	            e.setCancelled(true);
	            e.getPlayer().sendMessage("§a"+TRANSFORM_POTION);
	            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DIG_STONE, 10,1);
	            
	            potion.setLevel(1);
	            
	            PlayerInventory inv = e.getPlayer().getInventory();
	            inv.setItem(inv.getHeldItemSlot(), potion.toItemStack(1));
	        }
        }    
    }
}
