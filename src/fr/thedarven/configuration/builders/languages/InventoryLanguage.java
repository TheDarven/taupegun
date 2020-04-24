package fr.thedarven.configuration.builders.languages;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.events.ScenariosItemInteract;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.api.skull.Skull;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.messages.MessagesClass;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryLanguage extends InventoryGUI{

	private static String SELECTING_LANGUAGE = "Vous avez s�lectionn� la langue {languageName}";

	private InventoryLanguageElement selectedLanguage = null;
	private String defaultSelectedLanguage = null;
	
	public InventoryLanguage(String pName, String pDescription, String pTranslationName, Material pItem, InventoryGUI pParent, int pPosition, byte pData) {
		super(pName, pDescription, pTranslationName, 4, pItem, pParent, pPosition, pData);
		this.selectedLanguage = null;
		this.defaultSelectedLanguage = "fr_FR";
	}
	
	
	/**
	 * Pour mettre � jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		SELECTING_LANGUAGE = LanguageBuilder.getContent("MENU_LANGUAGE", "selectingMessage", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par d�faut
	 * 
	 * @return L'instance LanguageBuilder associ�e � l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		languageElement.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "selectingMessage", SELECTING_LANGUAGE);
		
		return languageElement;
	}
	
	
	/**
	 * Pour avoir le nom court de la langue selectionn�e
	 * 
	 * @return Le nom court de la langue selectionn�e
	 */
	public String getSelectedLanguage() {
		return this.selectedLanguage == null ? this.defaultSelectedLanguage : this.selectedLanguage.getLanguageShortName();
	}
	
	/**
	 * Pour changer l'inventaire de la langue selectionn�e
	 * 
	 * @param pSelectedLanguage Le nouveau inventaire langue selectionn�
	 */
	public void setSelectedLanguage(InventoryLanguageElement pSelectedLanguage) {
		this.selectedLanguage = pSelectedLanguage;
		reloadItem();
	}
	
	
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	public void reloadItem() {
		int exItem = getItem().hashCode();
		
		String link = selectedLanguage.getLink();
		link = link == null ? " " : link;
		
		ItemStack head = Skull.getCustomSkull(link, getItem());
		ItemMeta headM = head.getItemMeta();
		headM.setDisplayName(getFormattedItemName());
		head.setItemMeta(headM);
		
		updateItem(exItem, head);
	}
	
	
	
	/**
	 * Pour changer la langue selectionn�e
	 * 
	 * @param pSelectedLanguage L'inventaire de la langue selectionn�e
	 * @param p Le joueur qui a cliqu�
	 */
	private void changeSelectedLanguage(InventoryLanguageElement pSelectedLanguage, Player p) {
		if(pSelectedLanguage == this.selectedLanguage)
			return;
		
		InventoryLanguageElement exSelectedLanguage = this.selectedLanguage;
		String exName = ScenariosItemInteract.getFormattedScenariosItemName();
		
		setSelectedLanguage(pSelectedLanguage);
		ScenariosItemInteract.actionBeacon(exName);
		
		if(exSelectedLanguage != null)
			exSelectedLanguage.reloadItem();
		pSelectedLanguage.reloadItem();
		
		InventoryGUI.setLanguage();
		for(Player onlineP: Bukkit.getOnlinePlayers())
			MessagesClass.TabMessage(onlineP);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("languageName", "�6"+this.selectedLanguage.getName()+"�e");
		String selectingLanguageMessage = TextInterpreter.textInterpretation("�e"+SELECTING_LANGUAGE, params);
		
		Title.sendActionBar(p, selectingLanguageMessage);
	}
	
	/**
	 * L'�v�nement de clique dans l'inventaire
	 * 
	 * @param e L'�v�nement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(this.inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if(click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if(e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == this.getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					p.openInventory(this.getParent().getInventory());
					return;
				}
				for(InventoryGUI inventoryGUI : childs) {
					if(inventoryGUI.getItem().equals(e.getCurrentItem())) {
						if(inventoryGUI instanceof InventoryLanguageElement) {
							InventoryLanguageElement clickedInventory = (InventoryLanguageElement) inventoryGUI;
							if(click(p, EnumConfiguration.OPTION)) {
								changeSelectedLanguage(clickedInventory, p);
							}
						}else {
							p.openInventory(inventoryGUI.getInventory());
						}
						return;
					}
				}
				delayClick(pl);
			}
		}
	}	
}
