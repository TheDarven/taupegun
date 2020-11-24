package fr.thedarven.configuration.builders.teams;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.thedarven.configuration.builders.InventoryGUI;
import fr.thedarven.configuration.builders.InventoryRegister;
import fr.thedarven.main.metier.EnumConfiguration;
import fr.thedarven.main.metier.PlayerTaupe;
import fr.thedarven.utils.languages.LanguageBuilder;

public class InventoryParametres extends InventoryGUI{

protected static ArrayList<InventoryParametres> inventory = new ArrayList<>();
	
	private static String CHANGE_NAME = "Changer le nom";
	private static String CHANGE_COLOR = "Changer la couleur";

	public InventoryParametres(InventoryGUI pInventoryGUI) {
		super("Paramètres", null, "MENU_TEAM_ITEM_PARAMETER", 1, Material.REDSTONE_COMPARATOR, pInventoryGUI, 22);
		inventory.add(this);
		
		updateLanguage(InventoryRegister.language.getSelectedLanguage());
	}
	
	
	
	
	
	
	/**
	 * Pour mettre à jours les traductions de l'inventaire
	 * 
	 * @param language La langue
	 */
	public void updateLanguage(String language) {
		CHANGE_NAME = LanguageBuilder.getContent("TEAM", "changeName", language, true);
		CHANGE_COLOR = LanguageBuilder.getContent("TEAM", "changeColor", language, true);
		
		super.updateLanguage(language);
	}
	
	/**
	 * Pour initier des traductions par défaut
	 * 
	 * @return L'instance LanguageBuilder associée à l'inventaire courant.
	 */
	protected LanguageBuilder initDefaultTranslation() {
		LanguageBuilder languageElement = super.initDefaultTranslation();
		
		LanguageBuilder languageTeam = LanguageBuilder.getLanguageBuilder("TEAM");
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "changeName", CHANGE_NAME);
		languageTeam.addTranslation(LanguageBuilder.DEFAULT_LANGUAGE, "changeColor", CHANGE_COLOR);
		
		return languageElement;
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
	protected void reloadItem() {
		if (inventory == null || getInventory() == null)
			return;

		ItemStack nom = new ItemStack(Material.PAPER, 1);
		ItemMeta nomM = nom.getItemMeta();
		nomM.setDisplayName("§e"+CHANGE_NAME);
		nom.setItemMeta(nomM);
		getInventory().setItem(0, nom);

		ItemStack couleur = new ItemStack(Material.BANNER, 1, (byte) 15);
		ItemMeta couleurM = couleur.getItemMeta();
		couleurM.setDisplayName("§e"+CHANGE_COLOR);
		couleur.setItemMeta(couleurM);
		getInventory().setItem(1, couleur);
	}
	
	/**
	 * L'évènement de clique dans l'inventaire
	 * 
	 * @param e L'évènement de clique
	 */
	@EventHandler
	public void clickInventory(InventoryClickEvent e){
		if (!(e.getWhoClicked() instanceof Player) || e.getClickedInventory() == null || !e.getClickedInventory().equals(getInventory()))
			return;

		final Player p = (Player) e.getWhoClicked();
		PlayerTaupe pl = PlayerTaupe.getPlayerManager(p.getUniqueId());
		e.setCancelled(true);
		if (!click(p, EnumConfiguration.OPTION) || e.getCurrentItem().getType().equals(Material.AIR) || !pl.getCanClick())
			return;

		if (e.getCurrentItem().getType().equals(Material.REDSTONE) && e.getRawSlot() == getLines()*9-1 && e.getCurrentItem().getItemMeta().getDisplayName().equals(getBackName())){
			p.openInventory(getParent().getInventory());
			return;
		}
		delayClick(pl);
	}
	
}
