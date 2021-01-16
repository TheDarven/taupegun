package fr.thedarven.scenarios.kits;

import java.util.HashMap;
import java.util.Map;

import fr.thedarven.scenarios.InventoryGUI;
import fr.thedarven.scenarios.InventoryIncrement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.api.Title;
import fr.thedarven.utils.languages.LanguageBuilder;
import fr.thedarven.utils.texts.TextInterpreter;

public class InventoryKits extends InventoryIncrement {
	
	private static String TOO_LONG_NAME_FORMAT = "Le nom du kit ne doit pas dépasser 16 caractères.";
	private static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre kit.";
	private static String KIT_CREATE = "Le kit {kitName} a été crée avec succès.";
	private static String CREATE_KIT_NAME_FORMAT = "Nom du kit";
	
	public InventoryKits(InventoryGUI parent) {
		super("Kits", "Menu des kits.", "MENU_KIT", 2, Material.ENDER_CHEST, parent, 4);
	}

	
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("KIT", "nameTooLong", language, true);
		NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("KIT", "nameAlreadyUsed", language, true);
		KIT_CREATE = LanguageBuilder.getContent("KIT", "create", language, true);
		CREATE_KIT_NAME_FORMAT = LanguageBuilder.getContent("KIT", "createNameMessage", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "create", KIT_CREATE);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "createNameMessage", CREATE_KIT_NAME_FORMAT);
		
		return languageElement;
	}
	
	
	
	
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		for (InventoryGUI inv : getChildsValue())
			inv.getParent().removeItem(inv);

		int i = 0;
		for (InventoryGUI inv : getChildsValue()) {
			if (inv instanceof InventoryKitsElement) {
				modifiyPosition(inv,i);
				i++;
			} else if (getChilds().size() < 10){
				modifiyPosition(inv,getChilds().size()-1);
			}
		}
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
			final Player p = (Player) e.getWhoClicked();
			final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if (click(p, EnumConfiguration.OPTION) && e.getCurrentItem().equals(TaupeGun.getInstance().getInventoryRegister().addkits.getItem())) {
				e.setCancelled(true);
				new AnvilGUI(TaupeGun.getInstance(),p, new AnvilGUI.AnvilClickHandler() {
					
					@Override
				    public boolean onClick(AnvilGUI menu, String text) {
				    	pl.setCreateKitName(text);
				    	Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new Runnable() {
	
				    		@Override
				    		public void run() {
					    		if (pl.getCreateKitName().length() > 16){
					    			p.closeInventory();
					    			Title.sendActionBar(p, "§c"+TOO_LONG_NAME_FORMAT);			
					    			pl.setCreateKitName(null);
					    			return;
					    		}

								InventoryKitsElement matchedKit = InventoryKitsElement.getKit(pl.getCreateKitName());
					    		if(matchedKit != null) {
									p.openInventory(TaupeGun.getInstance().getInventoryRegister().kits.getInventory());
									Title.sendActionBar(p, "§c" + NAME_ALREADY_USED_FORMAT);
									pl.setCreateKitName(null);
									return;
								}
					    		
					    		InventoryKitsElement kit = new InventoryKitsElement(pl.getCreateKitName(), (InventoryKits) getParent());
					    		new InventoryDeleteKits(kit);
					    		
					    		Map<String, String> params = new HashMap<String, String>();
					    		params.put("kitName", "§e§l"+pl.getCreateKitName()+"§r§a");
					    		Title.sendActionBar(p, TextInterpreter.textInterpretation("§a"+KIT_CREATE, params));
					    		pl.setCreateKitName(null);
								p.openInventory(TaupeGun.getInstance().getInventoryRegister().kits.getLastChild().getInventory());
					    	}
				    	});
				    	return true;
				    }
				}).setInputName(CREATE_KIT_NAME_FORMAT).open();
			} else if (e.getClickedInventory().equals(getInventory())) {
				
				if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
					e.setCancelled(true);
					p.openInventory(getParent().getInventory());
					return;
				} else {
					InventoryGUI inventoryGUI = this.childs.get(e.getCurrentItem().hashCode());
					if (inventoryGUI != null) {
						e.setCancelled(true);
						if (inventoryGUI != TaupeGun.getInstance().getInventoryRegister().addkits || click(p, EnumConfiguration.OPTION)) {
							p.openInventory(inventoryGUI.getInventory());
							delayClick(pl);
						}
						return;
					}
				}
			}
		}
	}
	
}
