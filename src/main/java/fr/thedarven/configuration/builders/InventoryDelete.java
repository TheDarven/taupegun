package fr.thedarven.configuration.builders;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.kits.InventoryKitsElement;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;
import net.md_5.bungee.api.ChatColor;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class InventoryDelete extends InventoryGUI {

	private static String CONFIRM_ACTION = "✔ Confirmer";
	private static String CANCEL_ACTION = "✘ Annuler";
	
	public InventoryDelete(InventoryGUI pInventoryGUI, String pName, String pTranslationName, int pPosition) {
		super(pName, "", pTranslationName, 1, Material.STAINED_CLAY, pInventoryGUI, pPosition, (byte) 14);
		
		initItem();
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
		reloadItem();
	}
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		CONFIRM_ACTION = LanguageBuilder.getContent("CONTENT", "confirm", language, true);
		CANCEL_ACTION = LanguageBuilder.getContent("CONTENT", "cancel", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageContent = LanguageBuilder.getLanguageBuilder("CONTENT");
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "confirm", CONFIRM_ACTION);
		languageContent.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "cancel", CANCEL_ACTION);
		
		return languageElement;
	}
	
	
	
	
	/**
	 * Recharge les objets de l'inventaire
	 */
	public void reloadInventory() {
		clearChildsItems();

		AtomicInteger counter = new AtomicInteger(0);
		final int childsSize = getChilds().size();

		getChildsValue().forEach(child -> {
			if (child instanceof InventoryKitsElement) {
				modifiyPosition(child, counter.getAndIncrement());
			} else if (childsSize < 10){
				modifiyPosition(child,childsSize - 1);
			}
		});
	}
	
	/**
	 * Pour initier les items
	 */
	private void initItem() {
		getInventory().clear();
		ItemStack confirmer = new ItemStack(Material.STAINED_CLAY, 1, (byte) 13);
		ItemMeta confirmerM = confirmer.getItemMeta();
		confirmerM.setDisplayName(ChatColor.GREEN+CONFIRM_ACTION);
		confirmer.setItemMeta(confirmerM);
		getInventory().setItem(2, confirmer);
		
		ItemStack annuler = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		ItemMeta annulerM = annuler.getItemMeta();
		annulerM.setDisplayName(ChatColor.RED+CANCEL_ACTION);
		annuler.setItemMeta(annulerM);
		getInventory().setItem(6, annuler);
	}
	
	/**
	 * Pour mettre à jour des items dans l'inventaire
	 */
	protected void reloadItems() {
		super.reloadItems();
		reloadItem();
	}
	
	/**
	 * Pour recharger les items dans l'inventaire
	 */
	private void reloadItem() {
		if (inventory != null) {
			ItemStack confirmer = this.getInventory().getItem(2);
			if (confirmer != null) {
				ItemMeta confirmerM = confirmer.getItemMeta();
				confirmerM.setDisplayName(ChatColor.GREEN+CONFIRM_ACTION);
				confirmer.setItemMeta(confirmerM);
			}
			
			
			ItemStack annuler = this.getInventory().getItem(6);
			if (annuler != null) {
				ItemMeta annulerM = annuler.getItemMeta();
				annulerM.setDisplayName(ChatColor.RED+CANCEL_ACTION);
				annuler.setItemMeta(annulerM);
			}
		}
	}
	
	/**
	 * Pour supprimer un élément
	 * 
	 * @param p Le joueur qui supprime
	 */
	protected abstract void deleteElement(Player p);
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getClickedInventory().equals(inventory)) {
			Player p = (Player) e.getWhoClicked();
			PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
			e.setCancelled(true);
			
			if (click(p,EnumConfiguration.OPTION) && !e.getCurrentItem().getType().equals(Material.AIR) && pl.getCanClick()) {
				if (e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
					if (e.getCurrentItem().getDurability() == 13) {
						deleteElement(p);
					} else if (e.getCurrentItem().getDurability() == 14) {
						p.openInventory(getParent().getInventory());
					}
				}
				delayClick(pl);
			}
		}
	}
	
}
