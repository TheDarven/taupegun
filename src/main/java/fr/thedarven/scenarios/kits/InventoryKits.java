package fr.thedarven.scenarios.kits;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.PlayerTaupe;
import fr.thedarven.models.enums.EnumConfiguration;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.InventoryIncrement;
import fr.thedarven.scenarios.runnable.CreateKitRunnable;
import fr.thedarven.utils.api.AnvilGUI;
import fr.thedarven.utils.languages.LanguageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryKits extends InventoryIncrement {
	
	public static String TOO_LONG_NAME_FORMAT = "Le nom du kit ne doit pas dépasser 16 caractères.";
	public static String NAME_ALREADY_USED_FORMAT = "Le nom est déjà utilisé pour un autre kit.";
	public static String KIT_CREATE = "Le kit {kitName} a été crée avec succès.";
	public static String CREATE_KIT_NAME_FORMAT = "Nom du kit";
	
	public InventoryKits(InventoryGUI parent) {
		super("Kits", "Menu des kits.", "MENU_KIT", 2, Material.ENDER_CHEST, parent, 4);
	}







	@Override
	public void updateLanguage(String language) {
		TOO_LONG_NAME_FORMAT = LanguageBuilder.getContent("KIT", "nameTooLong", language, true);
		NAME_ALREADY_USED_FORMAT = LanguageBuilder.getContent("KIT", "nameAlreadyUsed", language, true);
		KIT_CREATE = LanguageBuilder.getContent("KIT", "create", language, true);
		CREATE_KIT_NAME_FORMAT = LanguageBuilder.getContent("KIT", "createNameMessage", language, true);
		
		super.updateLanguage(language);
	}

	@Override
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageKit = LanguageBuilder.getLanguageBuilder("KIT");
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameTooLong", TOO_LONG_NAME_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "nameAlreadyUsed", NAME_ALREADY_USED_FORMAT);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "create", KIT_CREATE);
		languageKit.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "createNameMessage", CREATE_KIT_NAME_FORMAT);
		
		return languageElement;
	}





	@Override
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

	@Override
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null) {
			final Player p = (Player) e.getWhoClicked();
			final PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			
			if (click(p, EnumConfiguration.OPTION) && e.getCurrentItem().equals(TaupeGun.getInstance().getInventoryRegister().addKit.getItem())) {
				e.setCancelled(true);

				final InventoryKits parent = this;

				new AnvilGUI(TaupeGun.getInstance(), p, (menu, text) -> {
					pl.setCreateKitName(text);
					Bukkit.getScheduler().runTask(TaupeGun.getInstance(), new CreateKitRunnable(p, pl, parent));
					return true;
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
						if (inventoryGUI != TaupeGun.getInstance().getInventoryRegister().addKit || click(p, EnumConfiguration.OPTION)) {
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
